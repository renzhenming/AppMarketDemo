package com.rzm.commonlibrary.general.navigationbar.impl;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.general.navigationbar.api.AbsNavigationBar;

/**
 * Created by renzhenming on 2018/4/2.
 */

public class ToolbarStyleNavigationBar extends AbsNavigationBar<ToolbarStyleNavigationBar.Builder.ToolbarParams> {


    protected ToolbarStyleNavigationBar(Builder.ToolbarParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.layout_toolbar;
    }

    @Override
    public void applyView() {
        setText(R.id.title,getParams().mTitle);
        setTextColor(R.id.title,getParams().mTitleTextColor);
        setTextSize(R.id.title,getParams().mTitleTextSize);

        setText(R.id.right,getParams().mRightText);
        setTextColor(R.id.right,getParams().mRightTextColor);
        setTextSize(R.id.right,getParams().mRightTextSize);
        setOnClickListener(R.id.right,getParams().mRightClickListener);

        setOnClickListener(R.id.left,getParams().mLeftClickListener);

        setBackgroundColor(R.id.background,getParams().mBackgroundColor);
    }

    public static class Builder extends AbsNavigationBar.Builder{


        private final ToolbarParams P;
        private final Context mContext;

        public Builder(Context context) {
            super(context, null);
            this.mContext = context.getApplicationContext();
            P = new ToolbarParams(context, null);
        }

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            this.mContext = context.getApplicationContext();
            P = new ToolbarParams(context,parent);
        }

        @Override
        public ToolbarStyleNavigationBar build() {
            return new ToolbarStyleNavigationBar(P);
        }

        public ToolbarStyleNavigationBar.Builder setBackgroundColor(int color){
            P.mBackgroundColor = ContextCompat.getColor(mContext,color);
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setTitleTextColor(int titleTextColor){
            P.mTitleTextColor = titleTextColor;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setTitleTextSize(int titleTextSize){
            P.mTitleTextSize = titleTextSize;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setRightText(String rightText){
            P.mRightText = rightText;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setRightTextColor(int rightTextColor){
            P.mRightTextColor = rightTextColor;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setRightTextSize(int rightTextSize){
            P.mRightTextSize = rightTextSize;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setOnRightClickListener(View.OnClickListener listener){
            P.mRightClickListener = listener;
            return this;
        }

        public ToolbarStyleNavigationBar.Builder setOnLeftClickListener(View.OnClickListener listener){
            P.mLeftClickListener = listener;
            return this;
        }

        public static class ToolbarParams extends AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightTextColor;
            public int mRightTextSize;
            public int mBackgroundColor;
            public int mTitleTextColor;
            public int mTitleTextSize;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)mContext).finish();
                }
            };

            public ToolbarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
