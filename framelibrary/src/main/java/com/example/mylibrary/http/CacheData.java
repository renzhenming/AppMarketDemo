package com.example.mylibrary.http;

/**
 * Created by renzhenming on 2017/8/26.
 */

public class CacheData {

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
