package com.rzm.commonlibrary.inject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by rzm on 2017/8/6.
 */

public class ViewBind {

    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }

    //兼容上边三个方法 object 反射需要执行的类
    public static void inject(ViewFinder finder,Object object){
        injectField(finder,object);
        injectEvent(finder,object);
    }

    /**
     * 注入事件
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {
        //1.获取类里所有的方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //2.获取OnClick的里面的value值（可点击view的id值）
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null){
                int[] viewIds = onClick.value();
                //3.find'ViewById找到view
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);
                    //判断是否添加网络状态判断的annotation
                    boolean checkNet = method.getAnnotation(CheckNet.class) != null;
                    if (view != null){
                        //4.view.setOnclickListener
                        view.setOnClickListener(new DeclaredOnClickListener(method,object,checkNet));
                    }
                }
            }
        }
    }

    public static class DeclaredOnClickListener implements View.OnClickListener{

        private final Object mObject;
        private final Method mMethod;
        private final boolean mCheckNet;

        public DeclaredOnClickListener(Method method, Object object, boolean checkNet) {
            this.mMethod = method;
            this.mObject = object;
            this.mCheckNet = checkNet;
        }

        @Override
        public void onClick(View view) {
            //执行之前先判断是否有网络，没有则返回不执行
            if (mCheckNet){
                if (!isNetWorkConn(view.getContext())){
                    Toast.makeText(view.getContext(),"请检查你的网络连接",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //5.反射执行方法 保证私有方法可用,
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject,view);
            } catch (Exception e) {
                e.printStackTrace();
                //如果执行有参方法失败，则执行无参方法，执行失败的原因是没有在方法中设置view参数，这时执行无参数方法即可
                //防止错误
                try {
                    mMethod.invoke(mObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 注入属性
     * @param finder
     * @param object
     */
    private static void injectField(ViewFinder finder, Object object) {
        //1.获取类中所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //2.获取view属性的id值（value)
        for (Field field : fields) {
            BindViewId bindViewId = field.getAnnotation(BindViewId.class);
            //防止id写错导致返回空值
            if (bindViewId != null){
                int viewId = bindViewId.value();
                //3.findviewbyid找到view
                View view = finder.findViewById(viewId);
                //4.动态注入找到的view
                field.setAccessible(true);
                try {
                    field.set(object,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //判断网络是否连接
    public static boolean isNetWorkConn(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(null!=info){
            return info.isConnected();
        }else {
            return false;
        }
    }
}
