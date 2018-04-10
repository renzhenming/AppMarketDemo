package com.app.rzm.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.view.Surface;


/**
 * 
 * 视频播放的控制器
 */
public class FFmpegUtils {

	public native void play(String input,Surface surface);
	
	/**
	 * 创建AudioTrack用于播放音频
	 * @param nb_channels 声道个数
	 * @return
	 */
	public AudioTrack createAudioTrack(int sampleRateInHz,int nb_channels){
		//声道布局
		int channelConfig;
		if(nb_channels == 1){
			channelConfig = android.media.AudioFormat.CHANNEL_OUT_MONO;
		}else if(nb_channels == 2){
			channelConfig = android.media.AudioFormat.CHANNEL_OUT_STEREO;
		}else{
			channelConfig = android.media.AudioFormat.CHANNEL_OUT_STEREO;
		}
		
		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		
		int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
 		@SuppressWarnings("deprecation")
		AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, AudioTrack.MODE_STREAM);
 		//播放
 		//audioTrack.play();
 		//写入PCM
 		//audioTrack.write(audioData, offsetInBytes, sizeInBytes);
 		return audioTrack;
	}
	
	static{
		/*System.loadLibrary("avutil-54");
		System.loadLibrary("swresample-1");
		System.loadLibrary("avcodec-56");
		System.loadLibrary("avformat-56");
		System.loadLibrary("swscale-3");
		System.loadLibrary("postproc-53");
		System.loadLibrary("avfilter-5");
		System.loadLibrary("avdevice-56");*/
		System.loadLibrary("myffmpeg");
	}
}
