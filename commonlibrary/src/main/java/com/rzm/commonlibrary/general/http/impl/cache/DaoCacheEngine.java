package com.rzm.commonlibrary.general.http.impl.cache;

import com.rzm.commonlibrary.general.db.DaoSupportFactory;
import com.rzm.commonlibrary.general.db.IDaoSupport;
import com.rzm.commonlibrary.general.http.base.CacheData;
import com.rzm.commonlibrary.utils.EncryptUtil;
import com.rzm.commonlibrary.general.http.base.IHttpCache;

import java.util.List;

/**
 * Created by renzhenming on 2018/3/25.
 *
 * 仅限进行网络请求的缓存，
 * 针对的是以url为key以服务器数据为value的接口返回数据
 */
public class DaoCacheEngine implements IHttpCache {

    /**
     * 获取缓存
     * @return
     */
    @Override
    public String getCache(String key) {
        String cacheJson = null;
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //以url作为参数，为了防止url中特殊字符无法被识别，所以需要进行md5加密
        List<CacheData> cacheList = daoSupport.querySupport().selection(CacheData.KEY).selectionArgs(EncryptUtil.toMD5(key)).query();
        if (cacheList.size() != 0) {
            CacheData cacheData = cacheList.get(0);
            cacheJson = cacheData.getmResultJson();
        }
        return cacheJson;
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean setCache(String key, String value) {
        //删掉原有的缓存
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        daoSupport.delete(CacheData.KEY, EncryptUtil.toMD5(key));
        long insert = daoSupport.insert(new CacheData(EncryptUtil.toMD5(key), value));
        return insert > 0;
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    @Override
    public boolean deleteCache(String key) {
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        int delete = daoSupport.delete(CacheData.KEY, EncryptUtil.toMD5(key));
        return delete > 0;
    }

    /**
     * 删除所有缓存
     * @return
     */
    @Override
    public boolean deleteAllCache() {
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        return daoSupport.deleteAll()>0;
    }

    /**
     * 更新缓存
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean updateCache(String newValue,String key, String value) {
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        int update = daoSupport.update(new CacheData(key, newValue), key, value);
        return update > 0;
    }
}
