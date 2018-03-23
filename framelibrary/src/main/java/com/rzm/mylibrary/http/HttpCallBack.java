package com.rzm.mylibrary.http;

import android.content.Context;

import com.google.gson.Gson;
import com.rzm.commonlibrary.general.http.EngineCallBack;
import com.rzm.commonlibrary.general.http.HttpUtils;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.Map;

/**
 * Created by rzm on 2017/8/20.
 * 回调业务逻辑层
 * 该回调是抽取出来的位于中间位置的操作
 *
 * 覆盖onPreExecute在开始请求之前执行一些每个网络接口都包含的公用部分，比如添加设备信息，app版本信息等等，
 * 这样的好处是在每个接口中就可以避免重复写这些重复的代码了
 *
 * 覆盖onSuccess完成泛型的使用，这里根据接口调用时（ .execute(new HttpCallBack<String>() ）中指定的
 * 泛型来进行对数据的解析，这样的好处是即使是同一个接口在返回的数据结构不同的情况下，我们也可以从容
 * 解析，提高了解析数据的稳健性，至于为什么不在上一层直接指定泛型呢，可以想象，如果那样做了，你可以解析
 * 的数据格式也就固定了，而这样做正好让数据的解析更加灵活
 *
 * 这就中间层处理业务逻辑的优点
 */

public abstract class HttpCallBack<T>  implements EngineCallBack {
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 大大方方的添加公用参数  与项目业务逻辑有关
        // 项目名称  context
        params.put("app_name","joke_essay");
        params.put("version_name","5.7.0");
        params.put("ac","wifi");
        params.put("device_id","30036118478");
        params.put("device_brand","Xiaomi");
        params.put("update_version_code","5701");
        params.put("manifest_version_code","570");
        params.put("longitude","113.000366");
        params.put("latitude","28.171377");
        params.put("device_platform","android");
        onPreExecute();
    }

    @Override
    public void onDownloadProgress(int progress) {
        downloadProgress(progress);
    }

    public void downloadProgress(int progress) {

    }

    @Override
    public void onUploadProgress(int progress) {
        uploadProgress(progress);
    }

    public void uploadProgress(int progress) {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T objResult = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);
    }

    //在执行成功数据解析之后再次执行success
    protected abstract void onSuccess(T result);

    //添加参数之后开始执行，回调过去
    public void onPreExecute(){

    }
}
