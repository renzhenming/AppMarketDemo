package com.app.rzm.utils;

/**
 * Created by rzm on 2017/10/28.
 * 增量更新
 */

public class BsUpdateUtils {
    static{
        System.loadLibrary("bspatch");
    }

    /**
     *
     * @param oldAPKPath 原来的apk 1.0
     * @param newAPKPath 合并后新的apk路径  2.0 差分包路径
     * @param patchPath 差分包路径 从服务器上下载的
     */
    public static native void combine(String oldAPKPath,String newAPKPath,String patchPath);


    /**
     * 新版和旧版生成差分包
     * @param oldAPKPath
     * @param newAPKPath
     * @param patchPath
     */
    public static native void diff(String oldAPKPath,String newAPKPath,String patchPath);
}
