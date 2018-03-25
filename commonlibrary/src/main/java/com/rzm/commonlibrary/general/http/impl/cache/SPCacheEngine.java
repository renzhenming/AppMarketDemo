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

    private final Context mContext;

    public SPCacheEngine(Context context) {
        this.mContext = context;
    }

    @Override
    public String getCache(String key) {
        return SharePreferenceUtil.getString(mContext, key);
    }

    @Override
    public boolean setCache(String key, String value) {
        return SharePreferenceUtil.setString(mContext,key,value);
    }

    @Override
    public boolean deleteCache(String key) {
        return SharePreferenceUtil.clear(mContext,key);
    }

    @Override
    public boolean deleteAllCache() {
        return SharePreferenceUtil.clearAll(mContext);
    }

    @Override
    public boolean updateCache(String newValue, String key, String value) {
        return SharePreferenceUtil.setString(mContext,key,value);
    }
}
