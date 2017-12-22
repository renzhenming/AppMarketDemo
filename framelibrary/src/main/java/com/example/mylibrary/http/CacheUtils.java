package com.example.mylibrary.http;

import com.example.mylibrary.db.DaoSupportFactory;
import com.example.mylibrary.db.IDaoSupport;
import com.rzm.commonlibrary.utils.EncryptUtil;

import java.util.List;

/**
 * Created by rzm on 2017/8/26.
 */

public class CacheUtils {

    public static String getCache(String key) {
        String cacheJson = null;
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        //以url作为参数，http://。。其中的“:”无法被识别，所以需要进行md5加密
        List<CacheData> cacheList = daoSupport.querySupport().selection("mUrlKey=?").selectionArgs(EncryptUtil.toMD5(key)).query();
        if (cacheList.size() != 0) {
            CacheData cacheData = cacheList.get(0);
            cacheJson = cacheData.getmResultJson();
        }
        return cacheJson;
    }

    public static long setCache(String key,String value){
        //删掉原有的缓存
        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        daoSupport.delete("mUrlKey=?", EncryptUtil.toMD5(key));
        return daoSupport.insert(new CacheData(EncryptUtil.toMD5(key), value));
    }
}
