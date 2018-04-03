package com.rzm.commonlibrary.general.http.base;


import android.content.Context;

/**
 * Created by rzm on 2018/3/25.
 * 缓存引擎封装，可以方便的切换缓存方式
 */
public class HttpCacheUtils {

    private static IHttpCache mHttpCache;

    private static HttpCacheUtils instance = new HttpCacheUtils();

    private HttpCacheUtils(){

    }

    public static HttpCacheUtils getInstance(){
        return instance;
    }

    /**
     * 初始化缓存引擎，在application的onCreate方法中设置一次即可
     * @param httpCache
     */
    public HttpCacheUtils init(IHttpCache httpCache){
        mHttpCache = httpCache;
        return this;
    }

    /**
     * 切换缓存引擎
     * @param httpCache
     * @return
     */
    public HttpCacheUtils exchangeCacheEngine(IHttpCache httpCache){
        mHttpCache = httpCache;
        return this;
    }

    public String getCache(Context context,String key) {
        if (mHttpCache == null){
            throw new NullPointerException("to use cache function ,please init IHttpCache first");
        }
        return mHttpCache.getCache(context,key);
    }

    public boolean setCache(Context context,String key,String value){
        if (mHttpCache == null){
            throw new NullPointerException("to use cache function ,please init IHttpCache first");
        }
        return mHttpCache.setCache(context,key,value);
    }

}
