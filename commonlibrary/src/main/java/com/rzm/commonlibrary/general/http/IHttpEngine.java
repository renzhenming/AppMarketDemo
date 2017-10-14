package com.rzm.commonlibrary.general.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by renzhenming on 2017/8/20.
 */

public interface IHttpEngine {

    //get请求
    void get(boolean cache ,Context context,String url, Map<String,Object> params, EngineCallBack callBack);

    //post请求
    void post(boolean cache ,Context context,String url, Map<String,Object> params,EngineCallBack callBack);

    // 取消请求
    // 下载文件
    // 上传文件
    // https添加安全证书
    // https 添加证书
}
