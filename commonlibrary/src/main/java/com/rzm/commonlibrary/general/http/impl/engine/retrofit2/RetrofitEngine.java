package com.rzm.commonlibrary.general.http.impl.engine.retrofit2;

import android.content.Context;
import android.text.TextUtils;

import com.rzm.commonlibrary.general.http.base.HttpCacheUtils;
import com.rzm.commonlibrary.general.http.base.ICallBack;
import com.rzm.commonlibrary.general.http.base.HttpUtils;
import com.rzm.commonlibrary.general.http.base.IHttpEngine;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.Map;

import retrofit2.*;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by rzm on 2017/11/1.
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
    public void get(final boolean cache, final Context context, String url, Map<String, Object> params, final ICallBack callBack) {
        final String paramsUrl = HttpUtils.jointParams(url, params);
        LogUtils.e(TAG,"get url:"+paramsUrl);

        if (cache) {
            final String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "get get cache ：" + cacheJson);
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
                    String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
                    //2.每次获取到的结果，和上次的缓存进行比对
                    if (!TextUtils.isEmpty(cacheJson)) {
                        if (result.equals(cacheJson)) {
                            //内容相同，没有数据更新，不需要执行成功方法刷新界面了
                            LogUtils.e(TAG, "no data updated");
                            return;
                        }
                    }
                }
                callBack.onSuccess(response.body().toString());

                if (cache) {
                    boolean success = HttpCacheUtils.getInstance().setCache(context,paramsUrl,result);
                    LogUtils.e(TAG, "get set cache -->> " + success+","+result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(new Exception(t.toString()));
            }
        });
    }

    @Override
    public void post(final boolean cache, final Context context, String url, Map<String, Object> params, final ICallBack callBack) {

        final String paramsUrl = HttpUtils.jointParams(url, params);
        LogUtils.e(TAG,"post url:"+paramsUrl);

        if (cache) {
            final String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "post get cache：" + cacheJson);
                callBack.onSuccess(cacheJson);
            }
        }

        api.post(url,params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                final String result = response.body().toString();
                LogUtils.e(TAG,"post result:"+result);

                if (cache) {
                    String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
                    //2.每次获取到的结果，和上次的缓存进行比对
                    if (!TextUtils.isEmpty(cacheJson)) {
                        if (result.equals(cacheJson)) {
                            //内容相同，没有数据更新，不需要执行成功方法刷新界面了
                            LogUtils.e(TAG, "no data updated");
                            return;
                        }
                    }
                }
                callBack.onSuccess(response.body().toString());

                if (cache) {
                    boolean success = HttpCacheUtils.getInstance().setCache(context,paramsUrl,result);
                    LogUtils.e(TAG, "post set cache -->> " + success+","+result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(new Exception(t.toString()));
            }
        });
    }

    @Override
    public void download(String url, ICallBack callBack) {

    }

    @Override
    public void upload(String path, String url, ICallBack callBack) {

    }

}
