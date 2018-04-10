#include "head/com_app_rzm_utils_FFmpegUtils.h"

#include <stdlib.h>
#include <stdio.h>
//usleep需要
#include <unistd.h>
#include <android/log.h>
//Surface相关
#include <android/native_window_jni.h>
#include <android/native_window.h>

#include<pthread.h>
#include <bits/timespec.h>

//被引入的libyuv中有C++代码编写，在这里需要设置为用C来编译

#include "include/libyuv/libyuv.h"

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"renzhenming",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"renzhenming",FORMAT,##__VA_ARGS__);

#define MAX_AUDIO_FRME_SIZE 48000 * 4

//封装格式
#include "include/ffmpeg/libavformat/avformat.h"
//解码
#include "include/ffmpeg/libavcodec/avcodec.h"
//缩放
#include "include/ffmpeg/libswscale/swscale.h"
//重采样
#include "include/ffmpeg/libswresample/swresample.h"

#include "queue.h"
#include "include/ffmpeg/libavutil/time.h"
#include "include/ffmpeg/libavutil/frame.h"
#include "include/ffmpeg/libavutil/audioconvert.h"

//nb_streams，视频文件中存在，音频流，视频流，字幕
#define MAX_STREAM 2
//音频重采样缓冲区大小
#define MAX_AUDIO_FRME_SIZE 48000 * 4

//队列中AVPacket的个数
#define PACKET_QUEUE_SIZE 50

typedef struct _Player Player;

typedef struct _DecodeData DecodeData;

#define MIN_SLEEP_TIME_US 1000ll
#define AUDIO_TIME_ADJUST_US -200000ll

struct _Player {
	//java虚拟机对象
	JavaVM *javaVM;

	//封装格式上下文
	AVFormatContext *avFormatCtx;
	//音频流视频流索引位置
	int video_stream_index;
	int audio_stream_index;
	//流的总个数
	int captrue_streams_num;
	//解码器上下文数组
	AVCodecContext *avCodecCtx[MAX_STREAM];
	//解码线程id
	pthread_t decode_threads[MAX_STREAM];
	//窗体绘制
	ANativeWindow* nativeWindow;

	SwrContext *swrCtx;
	//输入的采样格式
	enum AVSampleFormat in_sample_fmt;
	//输出采样格式16bit PCM
	enum AVSampleFormat out_sample_fmt;
	//输入采样率
	int in_sample_rate;
	//输出采样率
	int out_sample_rate;
	//输出的声道个数
	int out_channel_nb;

	//AudioTrack对象
	jobject audio_track;
	//AudioTrack类中的write方法的id
	jmethodID audio_track_write_mid;

	//生产者线程pthread_t
	pthread_t thread_read_from_stream;

	//队列数组
	Queue *packets[MAX_STREAM];

	//互斥锁
	pthread_mutex_t mutex;
	//条件变量
	pthread_cond_t cond;

	//视频开始播放的时间
	int64_t start_time;

	int64_t audio_clock;
};
void decode_audio(Player *player, AVPacket *avPacket);

void decode_video(Player *player, AVPacket *avPacket);
/**
 * 封装Player和stream_index，以便于在解码的时候可以根据当前这个Player
 * 的stream_index获取对应的队列，音频就取音频队列，视频就取视频队列
 */
struct _DecodeData {
	Player *player;
	int stream_index;
};

/**
 * 初始化封装格式上下文，获取音频视频流的索引位置
 */
void init_input_format_ctx(Player *player, const char* input_cstr) {
	//1.注册所有组件
	av_register_all();

	//封装格式上下文，统领全局的结构体，保存了视频文件封装格式的相关信息
	AVFormatContext *avFormatCtx = avformat_alloc_context();

	//2.打开输入视频文件(AVFormatContext **ps, const char *url, AVInputFormat *fmt, AVDictionary **options)
	if (avformat_open_input(&avFormatCtx, input_cstr, NULL, NULL) != 0) {
		LOGE("%s", "无法打开输入视频文件");
		return;
	}

	//3.获取视频信息(AVFormatContext *ic, AVDictionary **options)
	if (avformat_find_stream_info(avFormatCtx, NULL) < 0) {
		LOGE("%s", "无法获取视频文件信息");
		return;
	}

	//保存流的总个数
	player->captrue_streams_num = avFormatCtx->nb_streams;
	LOGI("captrue_streams_no:%d", player->captrue_streams_num);

	//4.获取音频和视频流的索引位置
	int i;
	for (i = 0; i < avFormatCtx->nb_streams; i++) {
		//视频
		if (avFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
			player->video_stream_index = i;
		}
		//音频
		if (avFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
			player->audio_stream_index = i;
		}
	}
	//5.将得到的上下文保存在结构体中
	player->avFormatCtx = avFormatCtx;
}

/**
 * 初始化解码器上下文
 *
 * stream_index:音频或者视频的index，获取对应的解码器
 */
void init_codec_context(Player *player, int stream_index) {
	AVFormatContext *avFormatCtx = player->avFormatCtx;

	//从视频流或音频流中获取对应的解码器上下文
	AVCodecContext *avCodecCtx = avFormatCtx->streams[stream_index]->codec;
	//根据编解码器id信息查找对应的解码器
	AVCodec *avCodec = avcodec_find_decoder(avCodecCtx->codec_id);

	if (avCodec == NULL) {
		LOGE("%s", "找不到解码器\n");
		return;
	}

	//打开解码器
	if (avcodec_open2(avCodecCtx, avCodec, NULL) < 0) {
		LOGE("%s", "解码器无法打开\n");
		return;
	}

	//将获取到的解码器上下文保存在结构体中，这个解码器包括音频和视频
	player->avCodecCtx[stream_index] = avCodecCtx;
}

//子线程解码
void decode_data(void* arg) {

	DecodeData *decode_data = (DecodeData *) arg;
	Player *player = decode_data->player;
	int stream_index = decode_data->stream_index;

	//根据stream_index获取对应的AVPacket队列
	Queue *queue = player->packets[stream_index];

	AVFormatContext *avFormatCtx = player->avFormatCtx;

	int video_frame_count = 0, audio_frame_count = 0;

	//每次读取一帧,存入avPacket
	for (;;) {
		//从队列中获取AVPacket

		//加锁
		pthread_mutex_lock(&player->mutex);
		AVPacket *packet = (AVPacket*) queue_pop(queue, &player->mutex,
				&player->cond);
		//解锁
		pthread_mutex_unlock(&player->mutex);

		if (stream_index == player->video_stream_index) {
			//解码视频
			decode_video(player, packet);
			LOGI("video_frame_count:%d", video_frame_count++);
		} else if (stream_index == player->audio_stream_index) {
			decode_audio(player, packet);
			LOGI("audio_frame_count:%d", audio_frame_count++);
		}

	}
	LOGI("解码完成");

}

/**
 * 解码音频准备
 */
void decode_audio_prepare(Player *player) {

	AVCodecContext *avCodecCtx = player->avCodecCtx[player->audio_stream_index];

	/**********  重采样设置参数---start   *****/

	//输入的采样格式
	enum AVSampleFormat in_sample_fmt = avCodecCtx->sample_fmt;
	//输出的采样格式16bit PCM
	enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
	//输入采样率
	int in_sample_rate = avCodecCtx->sample_rate;
	//输出的采样率设置为和输入相同
	int out_sample_rate = in_sample_rate;
	//输入的声道布局
	uint64_t in_ch_layout = avCodecCtx->channel_layout;
	//输出的声道布局(立体声，也可以设置为和输入相同)
	uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
	//输出的声道个数,从输出的声道布局中获取
	int out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);
	//frame->16bit 44100 PCM 统一音频采样格式与采样率
	SwrContext *swrCtx = swr_alloc();
	/**
	 * struct SwrContext *swr_alloc_set_opts(struct SwrContext *s,
	 *        int64_t out_ch_layout, enum AVSampleFormat out_sample_fmt, int out_sample_rate,
	 *        int64_t  in_ch_layout, enum AVSampleFormat  in_sample_fmt, int  in_sample_rate,
	 *        int log_offset, void *log_ctx);
	 */
	swr_alloc_set_opts(swrCtx, out_ch_layout, out_sample_fmt, out_sample_rate,
			in_ch_layout, in_sample_fmt, in_sample_rate, 0, NULL);
	swr_init(swrCtx);

	/**********  重采样设置参数---end   *****/

	//保存参数到结构体中
	player->in_sample_fmt = in_sample_fmt;
	player->out_sample_fmt = out_sample_fmt;
	player->in_sample_rate = in_sample_rate;
	player->out_sample_rate = out_sample_rate;
	player->out_channel_nb = out_channel_nb;
	player->swrCtx = swrCtx;
}

void jni_audio_prepare(JNIEnv *env, jobject jthiz, Player *player) {
	//获取播放器工具类class
	jclass player_class = (*env)->GetObjectClass(env, jthiz);

	//获取类中方法createAudioTrack的id
	jmethodID create_audio_track_id = (*env)->GetMethodID(env, player_class,
			"createAudioTrack", "(II)Landroid/media/AudioTrack;");

	//调用这个方法得到AudioTrack对象
	jobject audio_track = (*env)->CallObjectMethod(env, jthiz,
			create_audio_track_id, player->out_sample_rate,
			player->out_channel_nb);
	//调用AudioTrack.play方法
	jclass audio_track_class = (*env)->GetObjectClass(env, audio_track);

	//获取AudioTrack类中的play方法id
	jmethodID audio_track_play_mid = (*env)->GetMethodID(env, audio_track_class,
			"play", "()V");
	(*env)->CallVoidMethod(env, audio_track, audio_track_play_mid);

	//AudioTrack.write
	jmethodID audio_track_write_id = (*env)->GetMethodID(env, audio_track_class,
			"write", "([BII)I");

	//设置为全局引用，否则会报错,JNI ERROR(app bug):accessed stale local reference 0xxxxxxxx
	//在子线程中使用jobject或者jclass,要把相应的对象设置为全局引用，否则访问不到
	player->audio_track = (*env)->NewGlobalRef(env, audio_track);
	//(*env)->DeleteGlobalRef，注意全局引用使用完毕要delete
	player->audio_track_write_mid = audio_track_write_id;
}

/**
 * 解码音频
 */
void decode_audio(Player *player, AVPacket *avPacket) {
	//获取AVStream，并从中获取stream->time_base
	AVFormatContext *avFormatCtx = player->avFormatCtx;
	AVStream *stream = avFormatCtx->streams[player->video_stream_index];
	AVCodecContext *avCodecCtx = player->avCodecCtx[player->audio_stream_index];

	//开辟空间存储压缩数据
	AVFrame *avFrame = av_frame_alloc();
	//Zero if no frame could be decoded, otherwise it is non-zero
	int got_frame;
	/**
	 * int avcodec_decode_audio4(AVCodecContext *avctx, AVFrame *frame,
	 *      int *got_frame_ptr, const AVPacket *avpkt);
	 */
	avcodec_decode_audio4(avCodecCtx, avFrame, &got_frame, avPacket);
	//重采样缓冲区
	uint8_t *out_buffer = av_malloc(MAX_AUDIO_FRME_SIZE);
	//解码一帧成功
	if (got_frame) {
		/**
		 * int swr_convert(struct SwrContext *s, uint8_t **out, int out_count,
		 const uint8_t **in , int in_count);
		 */
		swr_convert(player->swrCtx, &out_buffer, MAX_AUDIO_FRME_SIZE,
				(const uint8_t **) avFrame->data, avFrame->nb_samples);
		/**
		 * 获取sample的size
		 * int av_samples_get_buffer_size(int *linesize, int nb_channels, int nb_samples,
		 enum AVSampleFormat sample_fmt, int align);
		 */
		int out_buffer_size = av_samples_get_buffer_size(NULL,
				player->out_channel_nb, avFrame->nb_samples,
				player->out_sample_fmt, 1);

		int64_t pts = avPacket->pts;
//		if(pts != AV_NOPTS_VALUE){TODO
//			player->audio_clock = av_rescale_q(pts,stream->time_base,AV_TIME_BASE_Q);
//			LOGI("player_write_audio - read from pts");
//			player_wait_for_frame(player,player->audio_clock+AUDIO_TIME_ADJUST_US,player->audio_stream_index);
//		}

		//关联当前线程的JNIEnv
		JavaVM *javaVM = player->javaVM;
		JNIEnv *env;
		(*javaVM)->AttachCurrentThread(javaVM, &env, NULL);

		//out_buffer缓冲区数据，转成byte数组,需要用到env,所以在上边获取
		//调用write方法需要传入一个byte数组，这个数组是包含out_buffer数组内容的，所以需要转换
		//创建一个和out_buffer数组同样大小的数组
		jbyteArray audio_sample_array = (*env)->NewByteArray(env,
				out_buffer_size);
		//获取可以操作这个数组的指针
		jbyte* sample_bytep = (*env)->GetByteArrayElements(env,
				audio_sample_array, NULL);
		//out_buffer的数据复制到sampe_bytep
		memcpy(sample_bytep, out_buffer, out_buffer_size);
		//同步
		(*env)->ReleaseByteArrayElements(env, audio_sample_array, sample_bytep,
				0);

		//AudioTrack.write PCM数据
		(*env)->CallIntMethod(env, player->audio_track,
				player->audio_track_write_mid, audio_sample_array, 0,
				out_buffer_size);
		//TODO
		//操作完成一次就释放一次数组，否则会溢出
		(*env)->DeleteLocalRef(env, audio_sample_array);
		(*javaVM)->DetachCurrentThread(javaVM);
		usleep(1000 * 16);

	}
	av_frame_free(&avFrame);
}

/**
 * 解码视频准备
 */
void decode_video_prepare(JNIEnv *env, Player *player, jobject surface) {
	//窗体  保存到结构体中
	player->nativeWindow = ANativeWindow_fromSurface(env, surface);
}
/**
 * 解码视频
 */
void decode_video(Player *player, AVPacket *avPacket) {
	//获取AVStream，并从中获取stream->time_base
	AVFormatContext *avFormatCtx = player->avFormatCtx;
	AVStream *stream = avFormatCtx->streams[player->video_stream_index];
	//开辟缓冲区AVFrame用于存储解码后的像素数据(YUV)
	AVFrame *yuv_Frame = av_frame_alloc();
	//开辟缓冲区AVFrame用于存储转成rgba8888后的像素数据(YUV)
	AVFrame *rgb_Frame = av_frame_alloc();
	//绘制时的缓冲区
	ANativeWindow_Buffer outBuffer;

	AVCodecContext *avCodecCtx = player->avCodecCtx[player->video_stream_index];

	//是否获取到视频像素数据的标记(Zero if no frame could be decompressed, otherwise, it is nonzero.)
	int got_picture;

	//7.解码一帧视频压缩数据，得到视频像素数据
	avcodec_decode_video2(avCodecCtx, yuv_Frame, &got_picture, avPacket);

	//为0说明全部解码完成，非0正在解码
	if (got_picture) {

		//设置缓冲区的属性    format 注意格式需要和surfaceview指定的像素格式相同
		ANativeWindow_setBuffersGeometry(player->nativeWindow,
				avCodecCtx->width, avCodecCtx->height, WINDOW_FORMAT_RGBA_8888);
		ANativeWindow_lock(player->nativeWindow, &outBuffer, NULL);

		//设置缓冲区像素格式,rgb_frame的缓冲区与outBuffer.bits时同一块内存
		avpicture_fill((AVPicture*) rgb_Frame, outBuffer.bits, PIX_FMT_RGBA,
				avCodecCtx->width, avCodecCtx->height);

		//按照yvu的顺序传参，如下，颜色正常，可以参照示例程序
		I420ToARGB(yuv_Frame->data[0], yuv_Frame->linesize[0],
				yuv_Frame->data[2], yuv_Frame->linesize[2], yuv_Frame->data[1],
				yuv_Frame->linesize[1], rgb_Frame->data[0],
				rgb_Frame->linesize[0], avCodecCtx->width, avCodecCtx->height);

		//计算延迟
		int64_t pts = av_frame_get_best_effort_timestamp(yuv_Frame);
		//不同时间基时间转换
		int64_t time = av_rescale_q(pts, stream->time_base, AV_TIME_BASE_Q);
		//等待time长的时间，时间结束，线程继续执行或者被唤醒继续执行 TODO
		//player_wait_for_frame(player, time, player->video_stream_index);
		//unlock
		ANativeWindow_unlockAndPost(player->nativeWindow);

		//每次都需要sleep一下，否则会播放一帧之后就崩溃
		usleep(1000 * 16);
	}

	av_frame_free(&yuv_Frame);
	av_frame_free(&rgb_Frame);
}

/**
 * 获取视频当前播放时间
 */
int64_t player_get_current_video_time(Player *player) {
	int64_t current_time = av_gettime();
	return current_time - player->start_time;
}
/**
 * 延迟
 */

void player_wait_for_frame(Player *player, int64_t stream_time, int stream_no) {
	pthread_mutex_lock(&player->mutex);
	for (;;) {
		int64_t current_video_time = player_get_current_video_time(player);
		int64_t sleep_time = stream_time - current_video_time;
		if (sleep_time < -300000ll) {
			// 300 ms late
			int64_t new_value = player->start_time - sleep_time;
			LOGI("player_wait_for_frame[%d] correcting %f to %f because late",
					stream_no, (av_gettime() - player->start_time) / 1000000.0,
					(av_gettime() - new_value) / 1000000.0);
			player->start_time = new_value;
			pthread_cond_broadcast(&player->cond);
		}
		if (sleep_time <= MIN_SLEEP_TIME_US) {
			// We do not need to wait if time is slower then minimal sleep time
			break;
		}

		if (sleep_time > 500000ll) {
			// if sleep time is bigger then 500ms just sleep this 500ms
			// and check everything again
			sleep_time = 500000ll;
		}
		//等待指定时长
        //pthread_cond_timedwait(&player->cond, &player->mutex, timespec);
		//int timeout_ret = pthread_cond_timeout_np(&player->cond, &player->mutex,
		//		sleep_time / 1000ll);

		// just go further
		LOGI("player_wait_for_frame[%d] finish", stream_no);
	}
	pthread_mutex_unlock(&player->mutex);
}

/**
 * 给AVPacket开辟空间，后面会将AVPacket栈内存数据拷贝至这里开辟的空间
 */
void *player_fill_packet() {
	AVPacket *packet = malloc(sizeof(AVPacket));
	return packet;
}

/**
 * 初始化音频视频队列，长度50
 */
void player_init_queues(Player *player) {
	int i;
	for (i = 0; i < player->captrue_streams_num; i++) {
		//有几个流要解析就初始化几个队列
		Queue *queue = queue_init(PACKET_QUEUE_SIZE,
				(queue_fill_fun) player_fill_packet);
		//将队列存入队列数组
		player->packets[i] = queue;
		//打印视频音频队列地址
		LOGI("stream index:%d,queue:%#x", i, queue);
	}
}

/**
 * 生产者线程：负责不断的读取视频文件中AVPacket，分别放入两个队列中
 */
void *player_read_stream(Player *player) {

	//栈内存上保存一个AVPacket
	AVPacket packet, *pkt = &packet;

	int ret;

	for (;;) {
		//int av_read_frame(AVFormatContext *s, AVPacket *pkt);
		//av_read_frame @return 0 if OK, < 0 on error or end of file
		ret = av_read_frame(player->avFormatCtx, pkt);

		//到文件结尾
		if (ret < 0) {
			break;
		}
		//根据AVPacket->stream_index获取对应的队列，
		Queue *queue = player->packets[pkt->stream_index];

		//示范队列内存释放
		//queue_free(queue,packet_free_fun);

		//加锁
		pthread_mutex_lock(&player->mutex);

		//将AVPacket压入队列
		AVPacket *packet_data = queue_push(queue, &player->mutex,
				&player->cond);

		//队列中当前的位置指向这个packet，保存地址,这样保存地址的话是不行的，
		//因为AVPacket packet是栈内存数据，所以当销毁后你保存这个栈内存的地址也是无效的，因为这个地址已经不是它了
		//packet_data = pkt;

		//拷贝（间接赋值，拷贝结构体数据）
		*packet_data = packet;

		//解锁
		pthread_mutex_unlock(&player->mutex);
		LOGI("queue:%#x, packet:%#x", queue, packet);
	}

}

JNIEXPORT void JNICALL Java_com_app_rzm_utils_FFmpegUtils_play(
		JNIEnv *env, jobject jobj, jstring input_jstr, jobject surface) {

	const char* input_cstr = (*env)->GetStringUTFChars(env, input_jstr, NULL);

	//给结构体申请一块空间(最后需要free掉)
	Player *player = (Player*) malloc(sizeof(Player));
	//获取Java虚拟机对象
	(*env)->GetJavaVM(env, &(player->javaVM));
	//初始化封装格式上下文
	init_input_format_ctx(player, input_cstr);

	int video_stream_index = player->video_stream_index;
	int audio_stream_index = player->audio_stream_index;
	//获取音视频解码器，并打开
	init_codec_context(player, video_stream_index);
	init_codec_context(player, audio_stream_index);

	decode_video_prepare(env, player, surface);
	decode_audio_prepare(player);

	jni_audio_prepare(env, jobj, player);

	//初始化队列
	player_init_queues(player);

	//初始化互斥锁int pthread_mutex_init(pthread_mutex_t *mutex,const pthread_mutexattr_t *attr);
	pthread_mutex_init(&player->mutex, NULL);

	//初始化条件变量int pthread_cond_init(pthread_cond_t *cond, const pthread_condattr_t *attr);
	pthread_cond_init(&player->cond, NULL);

	//生产者线程
	/**
	 * int pthread_create(pthread_t *thread, pthread_attr_t const * attr,
	 *          void *(*start_routine)(void *), void * arg)
	 */
	pthread_create(&(player->thread_read_from_stream), NULL, player_read_stream,
			(void *) player);
	//生产开始停留5，保证消费之前生产已经有了东西
	sleep(1);

	player->start_time = 0;

	//消费者线程,为什么要封装DecodeData加上stream_index，目的只是为了标明当前线程是要解码的是音频还是视频有了这个标记，
	//我们就可以获取对应的音频视频队列，因为解码的方法都是decode_data，所以要做区分
	DecodeData video_data = { player, video_stream_index }, *video_decode =
			&video_data;
	pthread_create(&(player->decode_threads[video_stream_index]), NULL,
			decode_data, (void*) video_decode);

	DecodeData audio_data = { player, audio_stream_index }, *audio_decode =
			&audio_data;
	pthread_create(&(player->decode_threads[audio_stream_index]), NULL,
			decode_data, (void*) audio_decode);

	//int pthread_join(pthread_t thid, void ** ret_val);
	//必须等待线程结束
	pthread_join(player->thread_read_from_stream, NULL);
	pthread_join(player->decode_threads[audio_stream_index], NULL);
	pthread_join(player->decode_threads[video_stream_index], NULL);

}

