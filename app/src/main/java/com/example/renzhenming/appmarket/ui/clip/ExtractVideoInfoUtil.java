package com.example.renzhenming.appmarket.ui.clip;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;


public class ExtractVideoInfoUtil {
    private MediaMetadataRetriever mMetadataRetriever;
    private long fileLength = 0;//毫秒

    public ExtractVideoInfoUtil(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                throw new RuntimeException("path must be not null !");
            }
            File file = new File(path);
            if (!file.exists()) {
                throw new RuntimeException("path file   not exists !");
            }
            mMetadataRetriever = new MediaMetadataRetriever();
            mMetadataRetriever.setDataSource(file.getAbsolutePath());
            String len = getVideoLength();
            fileLength = TextUtils.isEmpty(len) ? 0 : Long.valueOf(len);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    public int getVideoWidth() {
        String w = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        int width = -1;
        if (!TextUtils.isEmpty(w)) {
            width = Integer.valueOf(w);
        }
        return width;
    }

    public int getVideoHeight() {
        String h = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        int height = -1;
        if (!TextUtils.isEmpty(h)) {
            height = Integer.valueOf(h);
        }
        return height;
    }

    /**
     * 获取视频的典型的一帧图片，不耗时
     *
     * @return Bitmap
     */
    public Bitmap extractFrame() {
        return mMetadataRetriever.getFrameAtTime();
    }

    /**
     * 获取视频某一帧,不一定是关键帧
     *
     * @param timeMs 毫秒
     */
    public Bitmap extractFrame(long timeMs) {
        Bitmap bitmap = null;
        for (long i = timeMs; i < fileLength; i += 1000) {
            bitmap = mMetadataRetriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if (bitmap != null) {
                break;
            }
        }
        return bitmap;
    }



    /***
     * 获取视频的长度时间
     *
     * @return String 毫秒
     */
    public String getVideoLength() {
        try {
            return mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取视频旋转角度
     *
     * @return
     */
    public int getVideoDegree() {
        int degree = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            String degreeStr = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            if (!TextUtils.isEmpty(degreeStr)) {
                degree = Integer.valueOf(degreeStr);
            }
        }
        return degree;
    }

    public void release() {
        if (mMetadataRetriever != null) {
            mMetadataRetriever.release();
        }
    }

}
