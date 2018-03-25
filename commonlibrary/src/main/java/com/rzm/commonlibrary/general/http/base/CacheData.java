package com.rzm.commonlibrary.general.http.base;

/**
 * Created by rzm on 2017/8/26.
 * 用于缓存网络请求的json数据，这个实体只有两个属性，一个是缓存的key值，一个是json字符串
 */

public class CacheData {

    //字符串KEY必须和mUrlKey保持一致，因为存储的时候，就是用的下边这个字段作为key的
    public static final String KEY = "mUrlKey";

    //链接
    private String mUrlKey;

    //后台返回的json
    private String mResultJson;

    public CacheData() {

    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }

    public String getmUrlKey() {
        return mUrlKey;
    }

    public void setmUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getmResultJson() {
        return mResultJson;
    }

    public void setmResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }
}
