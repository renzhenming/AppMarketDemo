package com.app.rzm.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/4/16.
 * Version 1.0
 * Description: 图片处理
 */
public class ImageUtil {
    static {
        System.loadLibrary("jpeg");
        System.loadLibrary("compress_image");
    }

    /**
     * 图片压缩
     * @param bitmap 图片bitmap
     * @param quality 压缩的质量
     * @param fileName 压缩后的路径
     */
    public static void compressImage(Bitmap bitmap,int quality,
                                     String fileName){
        compressBitmap(bitmap,quality,fileName);
    }


    /**
     * NDK方法加载图片
     * @param bitmap 图片bitmap
     * @param quality 压缩的质量
     * @param fileName 压缩后的路径
     * @return
     */
    public native static int compressBitmap(Bitmap bitmap,int quality,
                                           String fileName);

    public static Bitmap decodeFile(String path) {
        int finalWidth = 800;

        // 先获取宽度
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 不加载图片到内存只拿宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        int bitmapWidth = options.outWidth;

        int inSampleSize = 1;

        if(bitmapWidth>finalWidth){
            inSampleSize = bitmapWidth/finalWidth;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path,options);
    }
}
