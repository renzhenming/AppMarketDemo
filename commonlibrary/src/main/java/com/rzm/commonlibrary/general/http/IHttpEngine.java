package com.rzm.commonlibrary.general.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by renzhenming on 2017/8/20.
 */

public interface IHttpEngine {

    //get请求
    void get(Context context,String url, Map<String,Object> params, EngineCallBack callBack);

    //post请求
    void post(Context context,String url, Map<String,Object> params,EngineCallBack callBack);
    //上传文件

    //https 添加证书
}
