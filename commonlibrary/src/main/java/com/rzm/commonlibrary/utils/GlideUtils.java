package com.rzm.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;


public class GlideUtils {

    private static String LAST_MODIFY_TIME = "lastheadModifyTime";

    public static void checkToFreeMemory(Context context) {
        long freeMem = Runtime.getRuntime().freeMemory();
        if (freeMem < 4 * 1024 * 1024) {
            Glide.get(context).clearMemory();
        }
    }

//    /**
//     * 针对imageview
//     * @param context
//     * @param url
//     * @param imageView
//     * @param defImage
//     * 添加signature，防止更新头像后glide显示的仍是旧头像（图片服务器地址不变，导致缓存的key不改变，这时候glide不会请求新的图片）
//     * signature 不能为null否则会崩溃
//     */
//    public static void setCircleTargetImage (final Activity context, final String url, String headModifyTime, final ImageView imageView, int defImage){
//        if (context == null)
//            return;
//        if (TextUtils.isEmpty(headModifyTime)){
//            SightPlusApplication application = (SightPlusApplication) context.getApplication();
//            headModifyTime = ProfileUtil.getHeadImageModifyTime(application);
//        }
//        if (TextUtils.isEmpty(headModifyTime)){
//            headModifyTime = String.valueOf(System.currentTimeMillis());
//        }
//
//        final String currentModifyTime = headModifyTime;
//        //获取上次缓存虚化背景的时间戳
//        final String lastModifyTime = SharedPreferencesUtil.getString(context, LAST_MODIFY_TIME);
//        Glide.with(context).load(url).asBitmap().signature(new StringSignature(headModifyTime)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defImage).centerCrop().into(new BitmapImageViewTarget(imageView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.build(context.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                imageView.setImageDrawable(circularBitmapDrawable);
//                if (!TextUtils.equals(currentModifyTime,lastModifyTime)){
//                    if (resource != null){
//                        blurAndSaveBitmap(resource,context);
//                        //缓存一次虚化头像后，保存当前的headModifyTime，当下一次走到这里，判断上次保存的时间戳和本次时间戳是否相同
//                        //如果相同说明头像没有更新，所以不需要再次缓存虚化背景，如果时间戳发生变化，说明头像已经更新，需要重新缓存
//                        SharedPreferencesUtil.insertString(context, LAST_MODIFY_TIME, currentModifyTime);
//                    }
//                }
//            }
//
//        });
//    }

    /**
     * 针对imageview
     *
     * @param context
     * @param url
     * @param imageView
     * @param defImage  添加signature，防止更新头像后glide显示的仍是旧头像（图片服务器地址不变，导致缓存的key不改变，这时候glide不会请求新的图片）
     *                  signature 不能为null否则会崩溃
     */
    /*public static void setCircleTargetImage(final Activity context, final String url, String headModifyTime, final ImageView imageView, int defImage) {
        if (context == null)
            return;
        if (TextUtils.isEmpty(headModifyTime)) {
            SightPlusApplication application = (SightPlusApplication) context.getApplication();
            headModifyTime = ProfileUtil.getHeadImageModifyTime(application);
        }
        if (TextUtils.isEmpty(headModifyTime)) {
            headModifyTime = String.valueOf(System.currentTimeMillis());
        }

        final String currentModifyTime = headModifyTime;
        //获取上次缓存虚化背景的时间戳
        final String lastModifyTime = SharedPreferencesUtil.getString(context, LAST_MODIFY_TIME);
        MainBitmapImageViewTarget mainTarget = new MainBitmapImageViewTarget(context, imageView, currentModifyTime, lastModifyTime);
        Glide.with(context).load(url).asBitmap().signature(new StringSignature(headModifyTime)).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defImage).centerCrop().into(mainTarget);
    }*/

    /**
     * 静态内部类，防止内存泄漏
     * 这里的上下文使用的是activity，需要若引用
     * 用于评论个人头像显示
     */
    private static class MainBitmapImageViewTarget extends BitmapImageViewTarget {

        private final String lastModifyTime;
        private final String currentModifyTime;

        private WeakReference<ImageView> reference;
        private WeakReference<Activity> cReference;

        public MainBitmapImageViewTarget(Activity context, ImageView imageView, String currentModifyTime, String lastModifyTime) {
            super(imageView);
            this.lastModifyTime = lastModifyTime;
            this.currentModifyTime = currentModifyTime;
            reference = new WeakReference<>(imageView);
            cReference = new WeakReference<>(context);
        }

        @Override
        protected void setResource(Bitmap resource) {
            super.setResource(resource);
            if (reference != null && cReference != null) {
                ImageView imageView = reference.get();
                Activity context = cReference.get();
                if (imageView != null && context != null) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                    if (!TextUtils.equals(currentModifyTime, lastModifyTime)) {
                        if (resource != null) {
                            blurAndSaveBitmap(resource, context);
                            //缓存一次虚化头像后，保存当前的headModifyTime，当下一次走到这里，判断上次保存的时间戳和本次时间戳是否相同
                            //如果相同说明头像没有更新，所以不需要再次缓存虚化背景，如果时间戳发生变化，说明头像已经更新，需要重新缓存
                            SpUtil.saveString(context, LAST_MODIFY_TIME, currentModifyTime);
                        }
                    }
                }
            }
        }
    }
//    /**
//     * 针对imageview
//     * @param context
//     * @param url
//     * @param imageView
//     * @param defImage
//     */
//    public static void setListImage (final Context context, final String url, final ImageView imageView, int defImage){
//        Glide.with(context).load(url).asBitmap().override(150,150).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defImage).centerCrop().into(new BitmapImageViewTarget(imageView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.build(context.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                imageView.setImageDrawable(circularBitmapDrawable);
//                //缓存详情的头像虚化效果
//                if (resource != null){
//                    BitmapView.saveCacheBitmap(context,url,resource);
//                }
//
//            }
//
//        });
//    }

    /**
     * 针对imageview
     * 用于评论列表页头像
     *
     * @param context
     * @param url
     * @param imageView
     * @param defImage
     */
    public static void setListImage(final Context context, final String url, final ImageView imageView, int defImage) {
        CuBitmapImageViewTarget target = new CuBitmapImageViewTarget(context, imageView, url);
        Glide.with(context).load(url).asBitmap().override(150, 150).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defImage).centerCrop().into(target);
    }

    /**
     * 静态内部类，防止内存泄漏
     * 这里的Context 为applicioncontext 不需要弱引用
     * 用于评论列表页头像
     */
    private static class CuBitmapImageViewTarget extends BitmapImageViewTarget {

        private final Context context;
        private final String imageUrl;
        private final ImageView imageView;

        public CuBitmapImageViewTarget(Context context, ImageView imageView, String imageUrl) {
            super(imageView);
            this.context = context;
            this.imageUrl = imageUrl;
            this.imageView = imageView;
        }

        @Override
        protected void setResource(Bitmap resource) {
            super.setResource(resource);
            /*if (imageView != null) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.build(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
                //缓存详情的头像虚化效果
                if (resource != null) {
                    BitmapView.saveCacheBitmap(context, imageUrl, resource);
                }
            }*/
        }
    }

    /**
     * bitmap虚化处理并缓存到本地
     *
     * @param bitmap
     * @param mActivity
     */
    public static void blurAndSaveBitmap(Bitmap bitmap, Activity mActivity) {
        /*Bitmap bluredBitmap = FastBlurUtil.toRectBlur(bitmap, 2, 2);
        String state = Environment.getExternalStorageState();
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            if (mActivity != null) {
                path = mActivity.getFilesDir().getAbsolutePath();
            }
        }
        if (!TextUtils.isEmpty(path)) {
            File dirFile = new File(path + "/CacheFile");
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile, "bluredBitmap");
            //保存地址,设置的时候直接去这里取，没有再请求网络
            SharedPreferencesUtil.insertString(mActivity, RequestWrapper.BLURED_HEAD_IMAGE, file.getAbsolutePath());
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bluredBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
    }

}
