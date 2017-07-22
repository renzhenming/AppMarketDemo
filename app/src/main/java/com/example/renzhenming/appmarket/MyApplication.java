package com.example.renzhenming.appmarket;

import android.app.Application;

/**
 * Created by renzhenming on 2017/7/22.
 */

public class MyApplication extends Application {

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static MyApplication getContext(){
        return context;
    }
}
