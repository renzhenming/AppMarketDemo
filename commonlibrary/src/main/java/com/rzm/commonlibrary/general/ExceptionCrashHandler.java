package com.rzm.commonlibrary.general;

import android.content.Context;

/**
 * Created by renzhenming on 2017/8/10.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private ExceptionCrashHandler(){}

    public static ExceptionCrashHandler getInstance(){
        if (mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if (mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    /**
     * 需要上传的信息
     *
     * 1.崩溃的详细信息
     * 2.应用信息 包名 版本号
     * 3.手机信息
     * 4.上传问题，上传文件不在这里处理，保存文件等应用再次启动时上传
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {





        //交由系统处理
        mDefaultUncaughtExceptionHandler.uncaughtException(thread,throwable);
    }
}
