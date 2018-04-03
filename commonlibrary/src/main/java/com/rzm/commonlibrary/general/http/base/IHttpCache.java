package com.rzm.commonlibrary.general.http.base;

import android.content.Context;

/**
 * Created by renzhenming on 2018/3/25.
 * 缓存引擎基类，指定缓存引擎的规则，需要被实现
 */

public interface IHttpCache {

    /**
     * 获取缓存
     * @return
     */
    String getCache(Context context, String key);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    boolean setCache(Context context,String key,String value);


    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean deleteCache(Context context,String key);

    /**
     * 删除所有缓存
     * @return
     */
    boolean deleteAllCache(Context context);

    /**
     * 更新缓存
     * @param key
     * @param value
     * @return
     */
    boolean updateCache(Context context,String newValue,String key,String value);
}
