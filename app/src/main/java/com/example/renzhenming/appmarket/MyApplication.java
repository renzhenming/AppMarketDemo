package com.example.renzhenming.appmarket;

import android.app.Application;

/**
 * Created by renzhenming on 2017/7/22.
 */

public class MyApplication extends Application {

    private static MyApplication context;
    private Thread.UncaughtExceptionHandler mHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            System.out.print("Exception in thread \""
                    + throwable.getMessage() + "\" ");

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Thread.setDefaultUncaughtExceptionHandler(mHandler);
    }

    public static MyApplication getContext(){
        return context;
    }
}
