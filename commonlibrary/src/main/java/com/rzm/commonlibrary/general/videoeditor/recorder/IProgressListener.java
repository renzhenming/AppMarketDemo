package com.rzm.commonlibrary.general.videoeditor.recorder;

/**
 * Created by liugang on 2017/11/17.
 */

public interface IProgressListener {

    void onMediaStart();

    void onMediaProgress(float progress);

    void onMediaDone();

    void onMediaPause();

    void onMediaStop();

    void onError(Exception exception);
}
