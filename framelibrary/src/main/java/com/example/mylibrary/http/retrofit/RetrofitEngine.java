package com.example.mylibrary.http.retrofit;

import android.content.Context;
import android.text.TextUtils;

import com.example.mylibrary.http.CacheUtils;
import com.example.mylibrary.http.HttpCallBack;
import com.rzm.commonlibrary.general.http.EngineCallBack;
import com.rzm.commonlibrary.general.http.HttpUtils;
import com.rzm.commonlibrary.general.http.IHttpEngine;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.Map;

import retrofit2.*;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by renzhenming on 2017/11/1.
 */

public class RetrofitEngine implements IHttpEngine {

    private static final String TAG = "RetrofitEngine";
    private final ResponseInfoApi api;

    public RetrofitEngine() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.baseUrl("http://www.baidu.com");
        Retrofit retrofit = builder.build();
        api = retrofit.create(ResponseInfoApi.class);
    }

    @Override
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String paramsUrl = HttpUtils.jointParams(url, params);
        LogUtils.e(TAG,"get url:"+paramsUrl);

        if (cache) {
            final String cacheJson = CacheUtils.getCache(paramsUrl);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "get 读取到缓存：" + cacheJson);
                //获取到缓存，直接执行成功方法
                callBack.onSuccess(cacheJson);
            }
        }

        api.get(url,params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                final String result = response.body().toString();
                LogUtils.e(TAG,"get result:"+result);

                if (cache) {
                    String cacheJson = CacheUtils.getCache(paramsUrl);
                    //2.每次获取到的结果，和上次的缓存进行比对
                    if (!TextUtils.isEmpty(cacheJson)) {
                        if (result.equals(cacheJson)) {
                            //内容相同，没有数据更新，不需要执行成功方法刷新界面了
                            LogUtils.e(TAG, "数据和缓存相同，不需要更新");
                            return;
                        }
                    }
                }
                callBack.onSuccess(response.body().toString());

                if (cache) {
                    long l = CacheUtils.setCache(paramsUrl, result);
                    LogUtils.e(TAG, "get 写入缓存 insert -->> " + l+","+result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(new Exception(t.toString()));
            }
        });
    }

    @Override
    public void post(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String paramsUrl = HttpUtils.jointParams(url, params);
        LogUtils.e(TAG,"post url:"+paramsUrl);

        if (cache) {
            final String cacheJson = CacheUtils.getCache(paramsUrl);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "post 读取到缓存：" + cacheJson);
                //获取到缓存，直接执行成功方法
                callBack.onSuccess(cacheJson);
            }
        }

        api.post(url,params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                final String result = response.body().toString();
                LogUtils.e(TAG,"post result:"+result);

                if (cache) {
                    String cacheJson = CacheUtils.getCache(paramsUrl);
                    //2.每次获取到的结果，和上次的缓存进行比对
                    if (!TextUtils.isEmpty(cacheJson)) {
                        if (result.equals(cacheJson)) {
                            //内容相同，没有数据更新，不需要执行成功方法刷新界面了
                            LogUtils.e(TAG, "数据和缓存相同，不需要更新");
                            return;
                        }
                    }
                }
                callBack.onSuccess(response.body().toString());

                if (cache) {
                    long l = CacheUtils.setCache(paramsUrl, result);
                    LogUtils.e(TAG, "post 写入缓存 insert -->> " + l+","+result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(new Exception(t.toString()));
            }
        });
    }

    @Override
    public void download(String url, EngineCallBack callBack) {

    }

    @Override
    public void upload(String path, String url, EngineCallBack callBack) {

    }

}
