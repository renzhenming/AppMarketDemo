package com.example.renzhenming.appmarket.ui.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtils {
    public static final String POSTFIX = ".jpeg";
    private static final String EDIT_PATH = "/EditVideo/";

    public static String saveImageToSD(Bitmap bmp, String dirPath) {
        if (bmp == null) {
            return "";
        }
        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }



    public static String saveImageToSDForEdit(Bitmap bmp, String dirPath, String fileName) {
        if (bmp == null) {
            return "";
        }
        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }
        }
        f.delete();
    }

    public static String getSaveEditThumbnailDir(Context context) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        File folderDir = new File(rootDir.getAbsolutePath() + EDIT_PATH);
        if (!folderDir.exists() && folderDir.mkdirs()) {

        }
        return folderDir.getAbsolutePath();
    }

    public static int getDisplayWidth(Context context) {
        if (context == null) {
            return 1;
        } else {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
    }

    public static int dip2px(Context context, int dpValue) {
        float scale = 0;
        try {
            scale = context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return dpValue;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(Context context, double dpValue) {
        float scale = 0;
        try {
            scale = context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return (int) dpValue;
        }
        return (int) (dpValue * scale + 0.5f);
    }

}
