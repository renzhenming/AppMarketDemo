package com.rzm.commonlibrary.general.videoeditor.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by cj on 2017/6/26 .
 */

public class PathUtils {

    private static String getSuitablePath(Context context) {
        String baseDir;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File baseDirFile = context.getExternalFilesDir(null);
            if (baseDirFile == null) {
                baseDir = context.getFilesDir().getAbsolutePath();
            } else {
                baseDir = baseDirFile.getAbsolutePath();
            }
        } else {
            baseDir = context.getFilesDir().getAbsolutePath();
        }
        return baseDir;
    }

    public static String getBaseFolder(Context context) {
        String baseFolder = Environment.getExternalStorageDirectory() + "/Codec/";
        File f = new File(baseFolder);
        if (!f.exists()) {
            boolean b = f.mkdirs();
            if (!b) {
                baseFolder = getSuitablePath(context) + "/";
            }
        }
        return baseFolder;
    }

    //获取VideoPath
    public static String getPath(Context context, String path, String fileName) {
        String p = getBaseFolder(context) + path;
        File f = new File(p);
        if (!f.exists() && !f.mkdirs()) {
            return getBaseFolder(context) + fileName;
        }
        return p + fileName;
    }
}
