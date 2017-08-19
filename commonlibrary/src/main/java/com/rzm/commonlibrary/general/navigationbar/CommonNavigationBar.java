package com.rzm.commonlibrary.general.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rzm.commonlibrary.R;

/**
 * Created by renzhenming on 2017/8/17.
 */

public class CommonNavigationBar extends AbsNavigationBar<CommonNavigationBar.Builder.CommonNavigationBarParams> {

    public CommonNavigationBar(CommonNavigationBar.Builder.CommonNavigationBarParams mParams) {
        super(mParams);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.common_navigation_bar;
    }

    @Override
    public void applyView() {
        //默认使用中间的title布局，如果设置了toolbar则使用toolbar
        if (getParams().mToolbarEnable){
            initToolbar(R.id.toolbar,getParams().mTitle);
            setOnToolbarClickListener(R.id.toolbar,getParams().mToolbarListener);
        }else{
            setText(R.id.center,getParams().mTitle);
        }
        setText(R.id.right_text,getParams().mRightText);
        setIcon(R.id.right_icon,getParams().mRightIcon);
        setOnClickListener(R.id.right_text,getParams().mRightListener);
        setOnClickListener(R.id.right_icon,getParams().mRightListener);

        setText(R.id.left_text,getParams().mLeftText);
        setIcon(R.id.left_icon,getParams().mLeftIcon);
        setOnClickListener(R.id.left_text,getParams().mLeftListener);
        setOnClickListener(R.id.left_icon,getParams().mLeftListener);

        //左边写一个默认的finish
        setOnClickListener(R.id.left_text,getParams().mLeftListener);

    }

    private void setIcon(int viewId, int mRightIcon) {
        ImageView iv = findViewById(viewId);
        if (mRightIcon != -1 && iv != null){
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(mRightIcon);
        }
    }

    private void initToolbar(int toolbarId, String mTitle) {
        Toolbar toolbar = mNavigationView.findViewById(toolbarId);
        if (toolbar != null && !TextUtils.isEmpty(mTitle)){
            AppCompatActivity activity = (AppCompatActivity) getParams().mContext;
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            toolbar.setTitle(mTitle);
        }
    }

    private void setText(int viewId, String mTitle) {
        TextView tv = findViewById(viewId);
        if (!TextUtils.isEmpty(mTitle) && tv != null){
            tv.setVisibility(View.VISIBLE);
            tv.setText(mTitle);
        }
    }

    private void setOnClickListener(int viewId,View.OnClickListener listener){
        View view = findViewById(viewId);
        if (view != null && listener != null)
            view.setOnClickListener(listener);
    }
    private void setOnToolbarClickListener(int viewId,View.OnClickListener listener){
        Toolbar view = (Toolbar)findViewById(viewId);
        if (view != null  && listener != null)
            view.setNavigationOnClickListener(listener);
    }

    public <T extends View>T findViewById(int viewId){
        return (T)mNavigationView.findViewById(viewId);
    }

    public static class Builder extends AbsNavigationBar.Builder{

        CommonNavigationBarParams P;

        //不穿parent，设置使用activity根节点 android.R.id.content
        public Builder(Context context) {
            super(context, null);
            P = new CommonNavigationBarParams(context,null);
        }

        //传入布局文件的根节点id作为navigation bar的parent
        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new CommonNavigationBarParams(context,parent);
        }


        public CommonNavigationBar.Builder setToolbarEnable(boolean toolbarEnable) {
            P.mToolbarEnable = toolbarEnable;
            return this;
        }

        public CommonNavigationBar.Builder setToolbarBackClickListener(View.OnClickListener listener) {
            P.mToolbarListener = listener;
            return this;
        }


        public CommonNavigationBar.Builder setTitle(String title){
            P.mTitle = title;
            return this;
        }

        public CommonNavigationBar.Builder setRightText(String rightText){
            P.mRightText = rightText;
            return this;
        }

        public CommonNavigationBar.Builder setRightIcon(int rightIcon){
            P.mRightIcon = rightIcon;
            return this;
        }

        public CommonNavigationBar.Builder setRightClickListener(View.OnClickListener listener){
            P.mRightListener = listener;
            return this;
        }

        public CommonNavigationBar.Builder seLeftText(String leftText){
            P.mLeftText = leftText;
            return this;
        }

        public CommonNavigationBar.Builder setLeftIcon(int leftIcon){
            P.mLeftIcon = leftIcon;
            return this;
        }

        public CommonNavigationBar.Builder setLeftClickListener(View.OnClickListener listener){
            P.mLeftListener = listener;
            return this;
        }

        @Override
        public CommonNavigationBar build() {
            CommonNavigationBar navigationBar = new CommonNavigationBar(P);
            return navigationBar;
        }

        public static class CommonNavigationBarParams extends AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightIcon = -1;
            public String mLeftText;
            public int mLeftIcon;
            public boolean mToolbarEnable;

            public View.OnClickListener mRightListener;
            //默认点击左侧finish activity ,重写后可覆盖
            public View.OnClickListener mLeftListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)mContext).finish();
                }
            };
            public View.OnClickListener mToolbarListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)mContext).finish();
                }
            };

            public CommonNavigationBarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
