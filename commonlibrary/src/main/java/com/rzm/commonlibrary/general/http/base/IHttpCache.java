package com.rzm.commonlibrary.general.http.base;

/**
 * Created by renzhenming on 2018/3/25.
 * 缓存引擎基类，指定缓存引擎的规则，需要被实现
 */

public interface IHttpCache {

    /**
     * 获取缓存
     * @return
     */
    String getCache(String key);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    boolean setCache(String key,String value);


    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean deleteCache(String key);

    /**
     * 删除所有缓存
     * @return
     */
    boolean deleteAllCache();

    /**
     * 更新缓存
     * @param key
     * @param value
     * @return
     */
    boolean updateCache(String newValue,String key,String value);
}
