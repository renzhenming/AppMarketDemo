package com.example.mylibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by rzm on 2017/8/30.
 * 皮肤资源管理
 */

public class SkinResource {

    private Resources mSkinResources;
    private String mSkinPackageName;

    public SkinResource(Context context, String skinPath) {
        try {
            //读取本地的一个 .skin的资源
            Resources sysResources = context.getResources();
            //反射获取资源管理器并指定资源路径
            AssetManager assets = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            //添加资源路径
            method.invoke(assets, skinPath);

            mSkinResources = new Resources(assets,sysResources.getDisplayMetrics(),sysResources.getConfiguration());
            //获取skin apk包的包名
            mSkinPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Drawable getDrawableByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName, "drawable", mSkinPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ColorStateList getColorByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName, "color", mSkinPackageName);
            ColorStateList color = mSkinResources.getColorStateList(resId);
            return color;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
