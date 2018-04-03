package com.rzm.commonlibrary.general.http.impl.cache;

import android.content.Context;

import com.rzm.commonlibrary.general.http.base.IHttpCache;
import com.rzm.commonlibrary.utils.SharePreferenceUtil;

/**
 * Created by renzhenming on 2018/3/25.
 *
 * 仅限进行网络请求的缓存，
 * 针对的是以url为key以服务器数据为value的接口返回数据
 */

public class SPCacheEngine implements IHttpCache {

    @Override
    public String getCache(Context context,String key) {
        return SharePreferenceUtil.getString(context, key);
    }

    @Override
    public boolean setCache(Context context,String key, String value) {
        return SharePreferenceUtil.setString(context,key,value);
    }

    @Override
    public boolean deleteCache(Context context,String key) {
        return SharePreferenceUtil.clear(context,key);
    }

    @Override
    public boolean deleteAllCache(Context context) {
        return SharePreferenceUtil.clearAll(context);
    }

    @Override
    public boolean updateCache(Context context,String newValue, String key, String value) {
        return SharePreferenceUtil.setString(context,key,value);
    }
}
