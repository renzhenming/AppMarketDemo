package com.rzm.commonlibrary.general.videoeditor.recorder;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by liugang on 2017/12/8.
 */

public class UnitySceneRecorder {

    private static final String TAG = "Capturing";

    private static Square texture;

    private int width = 0;
    private int height = 0;

    private int videoWidth = 0;
    private int videoHeight = 0;
    private int videoFrameRate = 0;

    private long nextCaptureTime = 0;
    private long startTime = 0;

    private static UnitySceneRecorder instance = null;

    private boolean finalizeFrame = false;
    private boolean isRunning = false;

    private IProgressListener progressListener = new IProgressListener() {
        @Override
        public void onMediaStart() {
            startTime = System.nanoTime();
            nextCaptureTime = 0;
            isRunning = true;
        }

        @Override
        public void onMediaProgress(float progress) {
        }

        @Override
        public void onMediaDone() {
        }

        @Override
        public void onMediaPause() {
        }

        @Override
        public void onMediaStop() {
        }

        @Override
        public void onError(Exception exception) {
        }
    };


    private VideoEncoder videoEncoder;
    private static final int FRAME_RATE = 15;               // 15fps


    public UnitySceneRecorder(Context context, int width, int height) {
        videoEncoder = new VideoEncoder(context, width, height);

        this.width = width;
        this.height = height;

        texture = new Square();
        instance = this;
    }

    public static UnitySceneRecorder getInstance() {
        return instance;
    }

    public static String getDirectoryDCIM() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator;
    }

    public void initCapturing(int width, int height, int frameRate, int bitRate) {
        Log.d(TAG, "--- initCapturing: " + width + "x" + height + ", " + frameRate + ", " + bitRate);
        videoFrameRate = frameRate;
        videoWidth = width;
        videoHeight = height;
    }

    public void startCapturing(final String videoPath) {
        Log.d(TAG, "--- startCapturing");

        if (videoEncoder == null) {
            return;
        }

        videoEncoder.start();
    }

    public void captureFrame(int textureID) {
        videoEncoder.pushFrame(textureID);
    }

    public void stopCapturing() {
        Log.d(TAG, "--- stopCapturing");
        isRunning = false;

        if (finalizeFrame) {
            finalizeFrame = false;
        }
        videoEncoder.stop();
    }

    public boolean isRunning() {
        return isRunning;
    }


    public int loadTextureFromNative(){
        return videoEncoder.textureGround;
    }
}
