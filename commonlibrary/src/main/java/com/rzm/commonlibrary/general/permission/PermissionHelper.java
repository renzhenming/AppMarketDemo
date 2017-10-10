package com.rzm.commonlibrary.general.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renzhenming on 2017/8/19.
 */

public class PermissionHelper {

    private final Object mObject;
    private int mRequestCode = 0;
    private String[] mRequestPermissions;
    private static List<String> mUnGrantedPermissions;

    public PermissionHelper(Object object){
        this.mObject = object;
    }

    //在activity中使用
    public static PermissionHelper with(Activity activity){
        return new PermissionHelper(activity);
    }

    //在fragment中使用
    public static PermissionHelper with(Fragment fragment){
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermissions(String ... permissions){
        this.mRequestPermissions = permissions;
        return this;
    }

    public void request(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            // 6.0以下直接执行方法  反射获取执行方法,这个方法是申请权限的回调方法，成功或失败,方法的名字不确定
            // 用注解的方式给方法打一个标记，然后通过反射去执行
            //(6.0以下的这个方法其实可以不去执行，直接配置文件设置以下就可以了，不过这样显得更规范，用与不用看个人喜好)
            executeBellow6();
        }else{
            executeOver6();
        }
    }

    private static Activity getContext(Object object) {
        Activity context = null;
        if (object instanceof Activity){
            context = ((Activity) object);
        }else if (object instanceof Fragment){
            context = ((Fragment) object).getActivity();
        }
        return context;
    }

    private void executeOver6() {
        //获取没有授权权限
        List<String> deniedPermissions = getDeniedPermissions(mObject,mRequestPermissions);
        if (deniedPermissions != null && deniedPermissions.size() == 0){
            //全部授权过
            executeSucceedMethod(mObject,mRequestCode);
        }else{
            //如果没有授予就申请权限
            requestListPermissions(mObject,deniedPermissions,mRequestCode);
        }
    }

    private static void requestListPermissions(Object object, List<String> deniedPermissions,int requestCode) {
        ActivityCompat.requestPermissions(getContext(object),deniedPermissions.toArray(new String[deniedPermissions.size()]),requestCode);
    }

    /*private static void requestListPermissions(Object object, List<String> deniedPermissions,int requestCode) {
        for (String deniedPermission : deniedPermissions) {
            ActivityCompat.requestPermissions(getContext(object),new String[]{deniedPermission},requestCode);
        }

    }*/

    private void executeBellow6() {
        if (mObject == null || mRequestCode == 0)
            return;
        executeSucceedMethod(mObject,mRequestCode);
    }

    private static void executeSucceedMethod(Object object, long requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            // 获取申请成功的标记
            PermissionSucceed permissionSucceed = method.getAnnotation(PermissionSucceed.class);
            if (permissionSucceed != null){
                int code = permissionSucceed.requestCode();
                //根据请求码和标记找到对应的方法
                if (requestCode == code){
                    try {
                        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
                        method.setAccessible(true);
                        method.invoke(object,new Object[]{});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void executeFailedMethod(Object object, long requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PermissionFailed permissionFailed = method.getAnnotation(PermissionFailed.class);
            if (permissionFailed !=null){
                int code = permissionFailed.requestCode();
                if (code == requestCode){
                    try {
                        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
                        method.setAccessible(true);
                        method.invoke(object,new Object[]{});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static List<String> getDeniedPermissions(Object mObject, String[] requestPermissions) {
        mUnGrantedPermissions = new ArrayList<>();
        Context context = getContext(mObject);
        if (context != null){
            for (String permission : requestPermissions) {
                if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                    mUnGrantedPermissions.add(permission);
                }
            }
        }
        return mUnGrantedPermissions;
    }

    /**
     * 处理申请权限的回调
     */
    public static void requestPermissionsResult(Object object, int requestCode,
                                                String[] permissions, int[] grantResults) {
        // 再次获取没有授予的权限
        List<String> deniedPermissions = getDeniedPermissions(object,permissions);
        if(deniedPermissions.size() == 0) {
            executeSucceedMethod(object, requestCode);
        }

        /*else{
            for (int i = 0; i < grantResults.length ; i ++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    //用户勾选了禁止权限
                    boolean b = ActivityCompat.shouldShowRequestPermissionRationale(getContext(object), deniedPermissions.get(i));
                    //判断是否勾选禁止后不再询问
                    if (b){
                        //拒绝了权限但是未选择下次不再提示，重新请求授权
                        requestListPermissions(object,deniedPermissions,requestCode);
                        return;
                    }else{
                        executeFailedMethod(object,requestCode);
                    }
                }
            }
        }*/

    }

    /**
     * 用户第一次点击一个需要权限的地方，该方法返回false(因为用户没拒绝~)，当用户拒绝掉该权限，下次点击此权限处，该方法会返回true
     * 当用户拒绝权限并勾选don't ask again选项后，会一直返回false，并且 ActivityCompat.requestPermissions 不会弹出对话框，
     * 系统直接deny，并执行 onRequestPermissionsResult 方法
     * @param activity
     * @param permission
     * @return
     */
    public static boolean shouldShowPermissions(Activity activity, List<String> permission) {

        for (String value : permission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    value)) {
                return true;
            }
        }
        return false;
    }

}
