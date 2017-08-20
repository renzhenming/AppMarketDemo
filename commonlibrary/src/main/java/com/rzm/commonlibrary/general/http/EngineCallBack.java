package com.rzm.commonlibrary.general.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by renzhenming on 2017/8/20.
 */

public interface EngineCallBack {

    void onPreExecute(Context context, Map<String,Object> params);

    void onError(Exception e);

    void onSuccess(String result);

    EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
