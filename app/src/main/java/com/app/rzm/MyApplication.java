package com.app.rzm;


import android.content.Context;

import com.rzm.commonlibrary.general.BaseApplication;
import com.rzm.commonlibrary.general.http.base.HttpUtils;
import com.rzm.commonlibrary.general.http.impl.cache.SPCacheEngine;
import com.rzm.commonlibrary.general.http.impl.engine.okhttp.OkHttpEngine;

/**
 * Created by rzm on 2017/7/22.
 */

public class MyApplication extends BaseApplication {

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        //设置网络引擎
        HttpUtils.initHttpEngine(new OkHttpEngine());
        HttpUtils.initCacheEngine(new SPCacheEngine());

/*.initCacheEngine(new SPCacheEngine(this)*/
        //HttpCacheUtils.initHttpEngine(new SPCacheEngine(this));

        /*PluginHelper.getInstance().applicationOnCreate(getBaseContext()); //must behind super.onCreate()
        try {
            PluginManager.getInstance().installPackage("", 0);
            PluginManager.getInstance().deletePackage("",0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static MyApplication getContext(){
        return context;
    }
}
