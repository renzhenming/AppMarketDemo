package com.rzm.commonlibrary.general.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

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

    public PermissionHelper(Object object) {
        this.mObject = object;
    }

    //在activity中使用
    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    //在fragment中使用
    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermissions(String... permissions) {
        this.mRequestPermissions = permissions;
        return this;
    }

    public void request() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
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
        if (object instanceof Activity) {
            context = ((Activity) object);
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        }
        return context;
    }

    private void executeOver6() {
        //获取没有授权权限
        List<String> deniedPermissions = getDeniedPermissions(mObject, mRequestPermissions);
        if (deniedPermissions != null && deniedPermissions.size() == 0) {
            //全部授权过
            executeSucceedMethod(mObject, mRequestCode);
        } else {
            //如果没有授予就申请权限
            requestListPermissions(mObject, deniedPermissions, mRequestCode);
        }
    }

    private static void requestListPermissions(Object object, List<String> deniedPermissions, int requestCode) {

        //默认是false,但是只要请求过一次权限就会为true,除非点了不再询问才会重新变为false
        if (shouldShowPermissions(getContext(object), deniedPermissions)) {
            Toast.makeText(getContext(object), "用户拒绝了一次权限，需要重新申请", Toast.LENGTH_SHORT).show();
        } else {
            // 无需向用户界面提示，直接请求权限,如果用户点了不再询问,即使调用请求权限也不会出现请求权限的对话框
            ActivityCompat.requestPermissions(getContext(object), deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
        }
    }

    private void executeBellow6() {
        if (mObject == null || mRequestCode == 0)
            return;
        executeSucceedMethod(mObject, mRequestCode);
    }

    private static void executeSucceedMethod(Object object, long requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            // 获取申请成功的标记
            PermissionSucceed permissionSucceed = method.getAnnotation(PermissionSucceed.class);
            if (permissionSucceed != null) {
                int code = permissionSucceed.requestCode();
                //根据请求码和标记找到对应的方法
                if (requestCode == code) {
                    try {
                        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
                        method.setAccessible(true);
                        method.invoke(object, new Object[]{});
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
            if (permissionFailed != null) {
                int code = permissionFailed.requestCode();
                if (code == requestCode) {
                    try {
                        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
                        method.setAccessible(true);
                        method.invoke(object, new Object[]{});
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
        if (context != null) {
            for (String permission : requestPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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

        if (verifyPermissions(grantResults)) {//有权限
            executeSucceedMethod(object, requestCode);
        } else {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getContext(object), permissions[i]);
                    if (showRequestPermission) {//这个返回false 表示勾选了不再提示
                        Toast.makeText(getContext(object), "用户拒绝了权限，并且选择了不再提醒，请去设置界面设置权限", Toast.LENGTH_SHORT).show();
                    } else {
                        //表示没有权限 ,但是没勾选不再提示
                        Toast.makeText(getContext(object), "请允许权限请求，才能进行下一步", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

    }

    /**
     * 判断请求权限是否成功
     *
     * @param grantResults
     * @return
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用户第一次点击一个需要权限的地方，该方法返回false(因为用户没拒绝~)，当用户拒绝掉该权限，下次点击此权限处，该方法会返回true
     * 当用户拒绝权限并勾选don't ask again选项后，会一直返回false，并且 ActivityCompat.requestPermissions 不会弹出对话框，
     * 系统直接deny，并执行 onRequestPermissionsResult 方法
     *
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

    /**
     * 检测这些权限中是否有 没有授权需要提示的
     */
    public static boolean shouldShowPermissions(Activity activity, String... permission) {

        for (String value : permission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    value)) {
                return true;
            }
        }
        return false;
    }

}
