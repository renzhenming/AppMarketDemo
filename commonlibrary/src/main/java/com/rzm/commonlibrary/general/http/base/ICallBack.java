package com.rzm.commonlibrary.general.http.base;

import android.content.Context;

import java.util.Map;

/**
 * Created by rzm on 2017/8/20.
 */

public interface ICallBack {

    void onPreExecute(Context context, Map<String,Object> params);

    void onError(Exception e);

    void onSuccess(String result);

    void onDownloadProgress(int progress);

    void onUploadProgress(int progress);

    ICallBack DEFAULT_CALL_BACK = new ICallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onDownloadProgress(int progress) {
        }

        @Override
        public void onUploadProgress(int progress) {

        }
    };
}
