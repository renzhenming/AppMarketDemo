package com.app.rzm;


import android.content.Context;

import com.rzm.commonlibrary.general.BaseApplication;

/**
 * Created by rzm on 2017/7/22.
 */

public class MyApplication extends BaseApplication {

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
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
