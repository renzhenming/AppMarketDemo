package com.app.rzm.ui.clip;

import android.os.Handler;


public class ExtractThread extends Thread {
    public static final int MSG_SAVE_SUCCESS = 0;
    private String videoPath;
    private String OutPutFileDirPath;
    private long startPosition;
    private long endPosition;
    private int thumbnailsCount;
    private VideoUtils mVideoUtils;

    public ExtractThread(int extractW, int extractH, Handler mHandler, String videoPath, String OutPutFileDirPath,
                         long startPosition, long endPosition, int thumbnailsCount) {
        this.videoPath = videoPath;
        this.OutPutFileDirPath = OutPutFileDirPath;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.thumbnailsCount = thumbnailsCount;
        this.mVideoUtils = new VideoUtils(extractW,extractH,mHandler);
    }

    @Override
    public void run() {
        super.run();
        mVideoUtils.getVideoThumbnailsInfoForEdit(
                videoPath,
                OutPutFileDirPath,
                startPosition,
                endPosition,
                thumbnailsCount);
    }

    public void stopExtract() {
        if (mVideoUtils != null) {
            mVideoUtils.stopExtract();
        }
    }
}
