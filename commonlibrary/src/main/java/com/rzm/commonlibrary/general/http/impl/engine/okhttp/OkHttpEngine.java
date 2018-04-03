package com.rzm.commonlibrary.general.http.impl.engine.okhttp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.rzm.commonlibrary.general.http.base.HttpCacheUtils;
import com.rzm.commonlibrary.general.http.base.ICallBack;
import com.rzm.commonlibrary.general.http.base.HttpUtils;
import com.rzm.commonlibrary.general.http.base.IHttpEngine;
import com.rzm.commonlibrary.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * Created by rzm on 2017/8/20.
 */
public class OkHttpEngine implements IHttpEngine {

    private static final String TAG = "OkHttpEngine";
    private static final String DOWNLOAD_SAVE_PATH = "Download_File";
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Handler mHandler = new Handler();


    /***************************
     *
     * post请求
     *
     ***************************/


    @Override
    public void post(final boolean cache, final Context context, String url, Map<String, Object> params, final ICallBack callBack) {

        final String paramsUrl = HttpUtils.jointParams(url, params);  //打印
        LogUtils.e(TAG, "post url:"+paramsUrl);

        if (cache) {
            final String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "post cache：" + cacheJson);
                //获取到缓存，直接执行成功方法
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(cacheJson);
                    }
                });

            }
        }

        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        // 两个回掉方法都不是在主线程中
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

                        final String result = response.body().string();
                        LogUtils.e(TAG,"post result:"+result);

                        if (cache) {
                            String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
                            //2.每次获取到的结果，和上次的缓存进行比对
                            if (!TextUtils.isEmpty(cacheJson)) {
                                if (result.equals(cacheJson)) {
                                    LogUtils.e(TAG, "data is the same with cache");
                                    return;
                                }
                            }
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });

                        if (cache) {
                            boolean success = HttpCacheUtils.getInstance().setCache(context,paramsUrl,result);
                            LogUtils.e(TAG, "post set cache -->> " + success+","+result);
                        }
                    }
                }
        );
    }


    /***************************
     *
     * get请求
     *
     ***************************/


    @Override
    public void get(final boolean cache, final Context context, String url, Map<String, Object> params, final ICallBack callBack) {

        final String paramsUrl = HttpUtils.jointParams(url, params);
        LogUtils.e(TAG,"get url:"+paramsUrl);

        if (cache) {
            final String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);

            if (!TextUtils.isEmpty(cacheJson)) {
                LogUtils.e(TAG, "get cache：" + cacheJson);

                //获取到缓存，直接执行成功方法
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        callBack.onSuccess(cacheJson);

                    }
                });

            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(paramsUrl).tag(context);
        //默认是GET请求
        Request request = requestBuilder.build();

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
                final String result = response.body().string();

                if (cache) {
                    String cacheJson = HttpCacheUtils.getInstance().getCache(context,paramsUrl);
                    //将获取到的结果和缓存对比，如果数据没有更新，就不再刷新
                    if (!TextUtils.isEmpty(cacheJson)) {
                        if (result.equals(cacheJson)) {
                            LogUtils.e(TAG, "data is the same with cache");
                            return;
                        }
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });

                LogUtils.e(TAG,"get result:"+ result);

                if (cache) {
                    boolean success = HttpCacheUtils.getInstance().setCache(context,paramsUrl,result);
                    LogUtils.e(TAG, "get set cache " + success+","+result);

                }
            }
        });
    }


    /***************************
     *
     * download请求
     *
     ***************************/


    @Override
    public void download(final String url,final ICallBack callBack) {
        Request request = new Request.Builder().url(url).build();
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
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(DOWNLOAD_SAVE_PATH);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        final int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onDownloadProgress(progress);
                            }
                        });
                    }
                    fos.flush();
                    // 下载完成
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess("");
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e);
                        }
                    });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /***************************
     *
     * upload请求
     *
     ***************************/

    @Override
    public void upload(String path, String url, final ICallBack callBack) {
        File file = new File(path);
        if (!file.exists()){
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), createCustomRequestBody(MultipartBody.FORM, file, new ProgressListener() {
                    @Override
                    public void onProgress(final long totalBytes, final long remainingBytes, boolean done) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onUploadProgress((int) ((totalBytes - remainingBytes) * 100 / totalBytes));
                            }
                        });
                    }
                }))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    public RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }


    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
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


}
