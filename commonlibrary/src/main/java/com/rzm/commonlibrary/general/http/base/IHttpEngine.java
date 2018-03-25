package com.rzm.commonlibrary.general.http.base;

import android.content.Context;

import java.util.Map;

/**
 * Created by rzm on 2017/8/20.
 *
 * 开闭原则：
 * 软件中的对象（类、模块、函数等）应该对于扩展是开放的，但是，对于修改是封闭的
 */

public interface IHttpEngine {

    //get请求
    void get(boolean cache ,Context context,String url, Map<String,Object> params, ICallBack callBack);

    //post请求
    void post(boolean cache ,Context context,String url, Map<String,Object> params,ICallBack callBack);

    // 取消请求
    // 下载文件
    void download(String url,ICallBack callBack);
    // 上传文件
    void upload(String path, String url,ICallBack callBack);
    // https添加安全证书
    // https 添加证书
}
