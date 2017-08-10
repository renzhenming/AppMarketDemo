package com.rzm.commonlibrary.general;

import android.app.Application;

/**
 * Created by renzhenming on 2017/8/10.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
    }
}
