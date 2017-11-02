package com.example.renzhenming.appmarket;


import android.content.Context;
import android.os.RemoteException;

import com.morgoo.droidplugin.PluginHelper;
import com.morgoo.droidplugin.pm.PluginManager;
import com.rzm.commonlibrary.general.BaseApplication;

/**
 * Created by renzhenming on 2017/7/22.
 */

public class MyApplication extends BaseApplication {

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PluginHelper.getInstance().applicationOnCreate(getBaseContext()); //must behind super.onCreate()
        try {
            PluginManager.getInstance().installPackage("", 0);
            PluginManager.getInstance().deletePackage("",0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }

    public static MyApplication getContext(){
        return context;
    }
}
