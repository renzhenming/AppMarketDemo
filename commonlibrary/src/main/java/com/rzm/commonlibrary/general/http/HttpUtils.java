package com.rzm.commonlibrary.general.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renzhenming on 2017/8/20.
 * 将各种网络引擎进行包装，后期如果需要跟换网络引擎，直接修改简单的代码就可以实现了
 */

public class HttpUtils{

    private String mUrl;

    private int mType = GET_TYPE;

    private static final int POST_TYPE = 0x0011;

    private static final int GET_TYPE = 0x0022;

    private Context mContext;

    private static IHttpEngine mHttpEngine;

    private Map<String,Object> mParams;

    private boolean mCache = false;

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

    //设置是否添加缓存
    public HttpUtils cache(boolean cache){
        mCache = cache;
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

        callBack.onPreExecute(mContext,mParams);

        //判断执行方法
        if (mType == POST_TYPE){
            post(mContext,mUrl,mParams,callBack);
        }
        if (mType == GET_TYPE){
            get(mContext,mUrl,mParams,callBack);
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
    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }

    /**
     * 这两个方法不提供在外边调用
     * @param url
     * @param params
     * @param callBack
     */
    private void get(Context context,String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mCache,context,url,params,callBack);
    }

    private void post(Context context,String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mCache,context,url,params,callBack);
    }

    //--------------------------------  供外界调用 --------------------------------//

    /**
     * 拼接参数
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
