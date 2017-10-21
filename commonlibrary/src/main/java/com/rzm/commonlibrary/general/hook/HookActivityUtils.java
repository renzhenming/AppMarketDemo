package com.rzm.commonlibrary.general.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.rzm.commonlibrary.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by renzhenming on 2017/10/17.
 */

public class HookActivityUtils {

    /** * hook start activity */
    public static void hookStartActivity(Context context,Class clazz) throws Exception{
        // 先获取ActivityManagerNative中的gDefault
        Class<?> amnClazz = Class.forName("android.app.ActivityManagerNative");
        Field defaultField = amnClazz.getDeclaredField("gDefault");
        defaultField.setAccessible(true);
        Object gDefaultObj = defaultField.get(null);
        // 获取Singleton里面的mInstance
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field amsField = singletonClazz.getDeclaredField("mInstance");
        amsField.setAccessible(true);
        Object amsObj = amsField.get(gDefaultObj);
        // 动态代理Hook下钩子
        amsObj = Proxy.newProxyInstance(context.getClass().getClassLoader(), amsObj.getClass().getInterfaces(), new StartActivityInvocationHandler(context,clazz,amsObj));
        // 注入
        amsField.set(gDefaultObj,amsObj);
    }

    /** * Start Activity Invocation Handler */
    private static class StartActivityInvocationHandler implements InvocationHandler {
        private final Context context;
        private final Class clazz;
        private Object mAmsObj;
        public StartActivityInvocationHandler(Context context, Class clazz, Object amsObj){
            this.context = context;
            this.clazz = clazz;
            this.mAmsObj = amsObj;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 拦截到所有ActivityManagerService的方法
            LogUtils.e("TAG","methodName"+method.getName());
            if(method.getName().equals("startActivity")){
                // 启动Activity的方法,找到原来的Intent
                Intent realIntent = (Intent) args[2];
                // 代理的Intent
                Intent proxyIntent = new Intent();
                proxyIntent.setComponent(new ComponentName(context,clazz));
                // 把原来的Intent绑在代理Intent上面
                proxyIntent.putExtra("realIntent",realIntent);
                // 让proxyIntent去晒太阳，借尸
                args[2] = proxyIntent;
            }
            return method.invoke(mAmsObj,args);

        }
    }

    /** * hook Launch Activity */
    public void hookLaunchActivity() throws Exception{
        // 获取ActivityThread
        Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);
        Object sCurrentActivityThreadObj = sCurrentActivityThreadField.get(null);
        // 获取Handler mH
        Field mHField = activityThreadClazz.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mH = (Handler) mHField.get(sCurrentActivityThreadObj);
        // 设置Callback
        Field callBackField = Handler.class.getDeclaredField("mCallback");
        callBackField.setAccessible(true);
        callBackField.set(mH, new ActivityThreadHandlerCallBack());
    }
    class ActivityThreadHandlerCallBack implements Handler.Callback {
        @Override public boolean handleMessage(Message msg) {
            if (msg.what == 100) {
                handleLaunchActivity(msg);
            }
            return false;
        }
    }
    // 还魂
    private void handleLaunchActivity(Message msg) {
        // final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
        try { Object obj = msg.obj;
            Field intentField = obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Intent proxyIntent = (Intent) intentField.get(obj);
            // 代理意图
            Intent originIntent = proxyIntent.getParcelableExtra("realIntent");
            if (originIntent != null) {
                // 替换意图
                intentField.set(obj, originIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void in(){
        try{
            ﻿// 兼容AppCompatActivity报错问题
            Class<?> forName = Class.forName("android.app.ActivityThread");
            Field field = forName.getDeclaredField("sCurrentActivityThread");
            field.setAccessible(true);
            Object activityThread = field.get(null);
            Method getPackageManager = activityThread.getClass().getDeclaredMethod("getPackageManager");
            Object iPackageManager = getPackageManager.invoke(activityThread);
            PackageManagerHandler handler = new PackageManagerHandler(iPackageManager);
            Class<?> iPackageManagerIntercept = Class.forName("android.content.pm.IPackageManager");
            Object proxy = newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{iPackageManagerIntercept}, handler);
            // 获取 sPackageManager 属性
            Field iPackageManagerField = activityThread.getClass().getDeclaredField("sPackageManager");
            iPackageManagerField.setAccessible(true); iPackageManagerField.set(activityThread, proxy);
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/


}
