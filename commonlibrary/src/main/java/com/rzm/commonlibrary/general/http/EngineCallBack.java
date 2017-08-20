package com.rzm.commonlibrary.general.http;

/**
 * Created by renzhenming on 2017/8/20.
 */

public interface EngineCallBack {

    void onError(Exception e);

    void onSuccess(String result);

    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
