package com.example.mylibrary.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.example.mylibrary.db.DaoSupportFactory;
import com.example.mylibrary.db.IDaoSupport;
import com.rzm.commonlibrary.general.http.EngineCallBack;
import com.rzm.commonlibrary.general.http.HttpUtils;
import com.rzm.commonlibrary.general.http.IHttpEngine;
import com.rzm.commonlibrary.utils.EncryptUtil;
import com.rzm.commonlibrary.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by renzhenming on 2017/8/20.
 */
public class OkHttpEngine implements IHttpEngine {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Handler mHandler = new Handler();

    @Override
    public void post(boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String jointUrl = HttpUtils.jointParams(url, params);  //打印
        LogUtils.e("Post请求路径：", jointUrl);

        // 了解 Okhhtp
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 这个 两个回掉方法都不是在主线程中
                        final String result = response.body().string();
                        LogUtils.e("Post返回结果：", jointUrl);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });
                    }
                }
        );
    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        LogUtils.e("Get请求路径：", url);

        if (cache) {
            final String cacheJson = CacheUtils.getCache(url);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e("TAG", "读取到缓存：" + cacheJson);
                //获取到缓存，直接执行成功方法
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(cacheJson);
                    }
                });

            }
        }
        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();

        final String finalUrl = url;
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();

                if (cache) {
                    String finalCacheJson = CacheUtils.getCache(finalUrl);
                    //2.每次获取到的结果，和上次的缓存进行比对
                    if (!TextUtils.isEmpty(finalCacheJson)) {
                        if (resultJson.equals(finalCacheJson)) {
                            //内容相同，没有数据更新，不需要执行成功方法刷新界面了
                            LogUtils.e("TAG", "数据和缓存相同，不需要更新");
                            return;
                        }
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(resultJson);
                    }
                });

                LogUtils.e("Get返回结果：", resultJson);

                if (cache) {
                    long l = CacheUtils.setCache(finalUrl, resultJson);
                    LogUtils.e("TAG", "insert -->> " + l);
                }
            }
        });
    }
}
