package com.rzm.commonlibrary.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

public class ServiceStateUtils {

    /**
     * 服务是否正在运行
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        // ActivityManager 相当于Windows里的任务管理器
        ActivityManager actManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的服务信息
        List<RunningServiceInfo> runningServices = actManager.getRunningServices(100);
        for (RunningServiceInfo runningServiceInfo : runningServices) {
            // 组件名称
            ComponentName serviceName = runningServiceInfo.service;
            if(TextUtils.equals(serviceName.getClassName(), serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }
}
