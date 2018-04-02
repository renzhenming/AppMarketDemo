package com.rzm.commonlibrary.general.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.rzm.commonlibrary.R;


/**
 * 用法：见READ_ME
 */
public class CommonNavigationBar<D extends
        CommonNavigationBar.Builder.DefaultNavigationParams> extends
        AbsNavigationBar<CommonNavigationBar.Builder.DefaultNavigationParams> {

    private CommonNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }


    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        setText(R.id.title, getParams().mTitle);
        setTextColor(R.id.title,getParams().mTitleTextColor);
        setTextSize(R.id.title,getParams().mTitleTextSize);

        setText(R.id.right_text, getParams().mRightText);
        setTextColor(R.id.right_text,getParams().mRightTextColor);
        setTextSize(R.id.right_text,getParams().mRightTextSize);

        setRightIcon(R.id.right_text,getParams().mRightIcon);
        setBackgroundColor(R.id.navigation_bar_parent,getParams().mBackgoundColor);
        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        // 左边 要写一个默认的  finishActivity
        setOnClickListener(R.id.back,getParams().mLeftClickListener);

        setVisibility(R.id.back,getParams().leftIconVisible);
    }




    public static class Builder extends AbsNavigationBar.Builder {

        private Context mContext;
        DefaultNavigationParams P;


        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            this.mContext = context.getApplicationContext();
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            this.mContext = context.getApplicationContext();
            P = new DefaultNavigationParams(context, null);
        }

        @Override
        public CommonNavigationBar build() {
            CommonNavigationBar navigationBar = new CommonNavigationBar(P);
            return navigationBar;
        }

        // 1. 设置所有效果

        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            P.mTitleTextColor = ContextCompat.getColor(mContext,titleTextColor);
            return this;
        }

        public Builder setTitleTextSize(float titleTextSize) {
            P.mTitleTextSize = titleTextSize;
            return this;
        }

        public Builder setTitleTextColor(String titleTextColor) {
            P.mTitleTextColor = Color.parseColor(titleTextColor);
            return this;
        }

        public Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public Builder setRightTextColor(int rightTextColor) {
            P.mRightTextColor = ContextCompat.getColor(mContext,rightTextColor);
            return this;
        }

        public Builder setRightTextColor(String rightTextColor) {
            P.mRightTextColor = Color.parseColor(rightTextColor);
            return this;
        }

        public Builder setRightTextSize(int rightTextSize) {
            P.mRightTextSize = rightTextSize;
            return this;
        }

        /**
         * 设置右边的点击事件
         */
        public Builder
        setRightClickListener(View.OnClickListener rightListener) {
            P.mRightClickListener = rightListener;
            return this;
        }

        /**
         * 设置左边的点击事件
         */
        public Builder
        setLeftClickListener(View.OnClickListener rightListener) {
            P.mLeftClickListener = rightListener;
            return this;
        }

        /**
         * 设置右边的图片
         */
        public Builder setRightIcon(int rightRes) {
            P.mRightIcon = rightRes;
            return this;
        }

        /**
         * 设置背景色
         */
        public Builder setBackgroundColor(int color) {
            P.mBackgoundColor = ContextCompat.getColor(mContext,color);
            return this;
        }

        /**
         * 设置背景色
         */
        public Builder setBackgroundColor(String color) {
            P.mBackgoundColor = Color.parseColor(color);
            return this;
        }

        public Builder hideLeftIcon() {
            P.leftIconVisible = View.INVISIBLE;
            return this;
        }

        public static class DefaultNavigationParams extends
                AbsNavigationBar.Builder.AbsNavigationParams {


            // 2.所有效果放置
            public String mTitle;

            public int mTitleTextColor = Color.parseColor("#000000");

            public float mTitleTextSize = 18;

            public String mRightText;

            public int mRightTextColor = Color.parseColor("#000000");

            public int mRightTextSize = 14;

            public int leftIconVisible = View.VISIBLE;

            public int mBackgoundColor = Color.parseColor("#ffffff");

            public int mRightIcon;

            // 后面还有一些通用的
            public View.OnClickListener mRightClickListener;

            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 关闭当前Activity
                    ((Activity) mContext).finish();
                }
            };


            private DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
