package com.rzm.commonlibrary.general.http;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renzhenming on 2017/8/20.
 */

public class HttpUtils{

    private String mUrl;

    private int mType = GET_TYPE;

    private static final int POST_TYPE = 0x0011;

    private static final int GET_TYPE = 0x0011;

    private Context mContext;

    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    private Map<String,Object> mParams;

    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    /**
     * 添加参数
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParams(String key,Object value){
        mParams.put(key,value);
        return this;
    }

    public HttpUtils addParams(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    public HttpUtils url(String url){
        mUrl = url;
        return this;
    }

    /**
     * 添加回掉 执行
     * @return
     */
    public HttpUtils execute(EngineCallBack callBack){
        if (callBack == null){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        //判断执行方法
        if (mType == POST_TYPE){
            post(mUrl,mParams,callBack);
        }
        if (mType == GET_TYPE){
            get(mUrl,mParams,callBack);
        }
        return this;
    }

    /**
     * 初始化引擎
     * @param httpEngine
     */
    public static void init(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    /**
     * 切换引擎
     * @param httpEngine
     */
    public static void exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    /**
     * 这两个方法不提供在外边调用
     * @param url
     * @param params
     * @param callBack
     */
    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(url,params,callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(url,params,callBack);
    }
}
