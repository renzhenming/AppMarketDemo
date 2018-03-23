package com.rzm.mylibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rzm.mylibrary.skin.SkinManager;
import com.rzm.mylibrary.skin.SkinResource;

/**
 * Created by rzm on 2017/8/30.
 */ 

public enum  SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            if (skinResource != null){
                ColorStateList color = skinResource.getColorByName(resourceName);
                if (color == null){
                    return;
                }
                TextView textView = (TextView) view;
                textView.setTextColor(color);
            }
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            //背景可能是图片，也可能是颜色
            if (skinResource != null){

                //如果是图片
                Drawable drawable = skinResource.getDrawableByName(resourceName);
                if (drawable != null){
                    ImageView imageView = (ImageView) view;
                    imageView.setBackgroundDrawable(drawable);
                    return;
                }

                //如果是颜色
                ColorStateList color = skinResource.getColorByName(resourceName);
                if (color != null){
                    view.setBackgroundColor(color.getDefaultColor());
                }
            }
        }
    },SRC("src") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resourceName);
            if (drawable != null){
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        }
    };

    private static SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }

    private final String mResName;

    SkinType(String resName){
        this.mResName = resName;
    }

    public abstract void skin(View view, String resourceName);

    public String getResName() {
        return mResName;
    }
}
