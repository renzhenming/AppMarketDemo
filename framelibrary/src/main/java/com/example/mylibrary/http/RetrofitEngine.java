package com.example.mylibrary.http;

import android.content.Context;

import com.rzm.commonlibrary.general.http.EngineCallBack;
import com.rzm.commonlibrary.general.http.IHttpEngine;

import java.util.Map;

/**
 * Created by renzhenming on 2017/11/1.
 */

public class RetrofitEngine implements IHttpEngine {
    @Override
    public void get(boolean cache, Context context, String url, Map<String, Object> params, EngineCallBack callBack) {

    }

    @Override
    public void post(boolean cache, Context context, String url, Map<String, Object> params, EngineCallBack callBack) {

    }

    @Override
    public void download(String url, EngineCallBack callBack) {

    }

    @Override
    public void upload(String path, String url, EngineCallBack callBack) {

    }

}
