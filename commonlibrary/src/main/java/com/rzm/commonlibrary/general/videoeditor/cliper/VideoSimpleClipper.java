package com.rzm.commonlibrary.general.videoeditor.cliper;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by liugang on 2017/12/5.
 */

public class VideoSimpleClipper {
    private MediaExtractor mediaExtractor;
    private MediaMuxer mediaMuxer;
    private MediaFormat mediaFormat;
    private String mime;

    private int sourceVTrack = 0;
    private int videoSourceInputSize = 0;
    private long videoDuration = 0;
    private int videoTrackIndex = -1;

    private int sourceATrack = 0;
    private int audioSourceInputSize = 0;
    private long audioDuration = 0;
    private int audioTrackIndex = -1;
    public static final String TAG = "VideoSimpleClipper";


    public interface OnVideoClipFinishListener {

        /**
         * 裁剪结果回调
         * @param success 是否裁剪成功
         * @param originPath   原始视频文件路径
         * @param clippedPath   裁剪后的视频文件保存路径
         */
        void onFinish(boolean success, String originPath, String clippedPath);
    }

    /**
     * 裁剪视频
     * @param videoPath 处理的视频原始文件
     * @param start 裁剪起点，单位为微妙，即1秒=1*1000*1000微妙
     * @param clipDur 裁剪时长，单位为微妙，即1秒=1*1000*1000微妙
     * @param onVideoClipFinishListener 结果回调
     */
    public static void clip(final String videoPath, final long start, final long clipDur, final OnVideoClipFinishListener onVideoClipFinishListener) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (onVideoClipFinishListener != null) {
                    String url=videoPath;
                    url=url.substring(0, url.lastIndexOf(".")) + "_output.mp4";
                    onVideoClipFinishListener.onFinish(msg.what == 1 ? true : false,videoPath,url);
                }
            }
        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                long s = System.currentTimeMillis();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    boolean b = new VideoSimpleClipper().decodeVideo(videoPath, start, clipDur);
                    handler.sendEmptyMessage(b == true ? 1 : 0);
                }
                Log.d(TAG, "clip finished,cost " + (System.currentTimeMillis() - s) / 1000 + "s");
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean decodeVideo(String url, long clipPoint, long clipDuration) {
        // 创建多媒体分离器
        mediaExtractor = new MediaExtractor();
        try {
            // 设置多媒体数据源
            mediaExtractor.setDataSource(url);
            // 创建合成器
            mediaMuxer = new MediaMuxer(url.substring(0, url.lastIndexOf(".")) + "_output.mp4",
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mediaMuxer.setOrientationHint(90);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 获取每个轨道信息
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
            try {
                mediaFormat = mediaExtractor.getTrackFormat(i);
                mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("video/")) {
                    sourceVTrack = i;

                    int width = mediaFormat.getInteger(MediaFormat.KEY_WIDTH);
                    int height = mediaFormat.getInteger(MediaFormat.KEY_HEIGHT);
                    videoSourceInputSize = mediaFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    videoDuration = mediaFormat.getLong(MediaFormat.KEY_DURATION);

                    // 剪切点超过了视频最大长度
                    if (clipPoint >= videoDuration) {
                        return false;
                    }

                    if ((clipDuration != 0) && (clipPoint + clipDuration) >= videoDuration) {
                        return false;
                    }

                    // 向合成器中添加视频轨
                    videoTrackIndex = mediaMuxer.addTrack(mediaFormat);

                } else if (mime.startsWith("audio/")) {
                    sourceATrack = i;
                    int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                    int channelCount = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                    audioSourceInputSize = mediaFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    audioDuration = mediaFormat.getLong(MediaFormat.KEY_DURATION);
                    // 向合成器中添加音频轨
                    audioTrackIndex = mediaMuxer.addTrack(mediaFormat);
                }

            } catch (Exception e) {
                return false;
            }

        }
        // start()方法一定要在addTrack之后
        mediaMuxer.start();

        // 分配缓冲
        ByteBuffer inputBuffer = ByteBuffer.allocate(videoSourceInputSize);

        // 1. 先进行视频数据处理
        mediaExtractor.selectTrack(sourceVTrack);
        MediaCodec.BufferInfo videoInfo = new MediaCodec.BufferInfo();
        videoInfo.presentationTimeUs = 0;
        long lastSamplePTS = 0;

        // 计算采样（帧）率
        long videoSampleTime;
        {
            // 获取源视频相邻帧之间的时间间隔
            mediaExtractor.readSampleData(inputBuffer, 0);

            // 跳过第一个I帧
            if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                mediaExtractor.advance();
            }

            mediaExtractor.readSampleData(inputBuffer, 0);
            long firstVideoPTS = mediaExtractor.getSampleTime();

            mediaExtractor.advance();
            mediaExtractor.readSampleData(inputBuffer, 0);
            long secondPTS = mediaExtractor.getSampleTime();
            videoSampleTime = Math.abs(secondPTS - firstVideoPTS);
        }

        // 重新切换此信道，因为上面已经跳过3帧，会造成前面的帧模糊
        mediaExtractor.unselectTrack(sourceVTrack);
        mediaExtractor.selectTrack(sourceVTrack);

        // 选择起点
        mediaExtractor.seekTo(clipPoint, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);

        while (true) {
            int sampleSize = mediaExtractor.readSampleData(inputBuffer, 0);
            if (sampleSize < 0) {
                // 这里一定要释放选择的轨道，不然另一个轨道就无法选中了
                mediaExtractor.unselectTrack(sourceVTrack);
                break;
            }

            int trackIndex = mediaExtractor.getSampleTrackIndex();

            // 获取时间戳
            long presentationTimeUs = mediaExtractor.getSampleTime();
            if (lastSamplePTS == 0) {
                lastSamplePTS = presentationTimeUs;
            }
            // 获取帧类型，只能识别是否是关键帧
            int sampleFlag = mediaExtractor.getSampleFlags();

            // 剪辑时间到了，跳出
            if ((clipDuration != 0) && (presentationTimeUs > (clipPoint + clipDuration))) {
                mediaExtractor.unselectTrack(sourceVTrack);
                break;
            }

            mediaExtractor.advance();
            videoInfo.offset = 0;
            videoInfo.size = sampleSize;
            videoInfo.flags = sampleFlag;
            videoInfo.presentationTimeUs = presentationTimeUs;
            mediaMuxer.writeSampleData(videoTrackIndex, inputBuffer, videoInfo);
        }

        // 2. 音频处理
        mediaExtractor.selectTrack(sourceATrack);
        MediaCodec.BufferInfo audioInfo = new MediaCodec.BufferInfo();
        audioInfo.presentationTimeUs = 0;
        long audioSampleTime;
        {
            // 获取音频帧时长
            mediaExtractor.readSampleData(inputBuffer, 0);

            // 跳过第一个sample
            if (mediaExtractor.getSampleTime() == 0) {
                mediaExtractor.advance();
            }

            mediaExtractor.readSampleData(inputBuffer, 0);
            long firstAudioPTS = mediaExtractor.getSampleTime();

            mediaExtractor.advance();
            mediaExtractor.readSampleData(inputBuffer, 0);
            long secondAudioPTS = mediaExtractor.getSampleTime();

            audioSampleTime = Math.abs(secondAudioPTS - firstAudioPTS);
        }

        // 重新切换此信道
        mediaExtractor.unselectTrack(sourceATrack);
        mediaExtractor.selectTrack(sourceATrack);

        mediaExtractor.seekTo(clipPoint, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        while (true) {
            int sampleSize = mediaExtractor.readSampleData(inputBuffer, 0);
            if (sampleSize < 0) {
                mediaExtractor.unselectTrack(sourceATrack);
                break;
            }

            int trackIndex = mediaExtractor.getSampleTrackIndex();

            long presentationTimeUs = mediaExtractor.getSampleTime();
            // 剪辑时间到了，跳出
            if ((clipDuration != 0) && (presentationTimeUs > (clipPoint + clipDuration))) {
                mediaExtractor.unselectTrack(sourceATrack);
                break;
            }

            mediaExtractor.advance();
            audioInfo.offset = 0;
            audioInfo.size = sampleSize;
            //audioInfo.presentationTimeUs += audioSampleTime;
            audioInfo.presentationTimeUs = presentationTimeUs;
            mediaMuxer.writeSampleData(audioTrackIndex, inputBuffer, audioInfo);
        }

        // 全部写完后，释放MediaExtractor和MediaMuxer
        mediaMuxer.stop();
        mediaMuxer.release();
        mediaExtractor.release();
        mediaExtractor = null;
        return true;
    }
}
