package com.rzm.commonlibrary.general.navigationbar.api;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rzm.commonlibrary.utils.LogUtils;

/**
 * Created by rzm on 2017/8/20.
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private static final String TAG = "AbsNavigationBar";

    private P mParams;

    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }


    public P getParams() {
        return mParams;
    }


    /**
     * 设置文本
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if(!TextUtils.isEmpty(text) && tv != null){
            if (tv.getVisibility() != View.VISIBLE)
                tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    /**
     * 设置文本颜色
     * @param viewId
     * @param textColor
     */
    protected void setTextColor(int viewId, int textColor) {
        TextView tv = findViewById(viewId);
        if(tv != null){
            if (tv.getVisibility() != View.VISIBLE)
                tv.setVisibility(View.VISIBLE);
            tv.setTextColor(textColor);
        }
    }

    /**
     * 设置文本字体大小
     * @param viewId
     * @param textSize
     */
    protected void setTextSize(int viewId, float textSize) {
        TextView tv = findViewById(viewId);
        if(tv != null){
            if (tv.getVisibility() != View.VISIBLE)
                tv.setVisibility(View.VISIBLE);
            tv.setTextSize(textSize);
        }
    }

    protected void setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        if (view != null)
            view.setVisibility(visibility);
    }

    /**
     * 设置背景色
     * @param viewId
     * @param color
     */
    protected void setBackgroundColor(int viewId, int color) {
        ViewGroup viewGroup = findViewById(viewId);
        if(viewGroup != null){
            viewGroup.setBackgroundColor(color);
        }
    }

    /**
     * 设置右边的icon
     * @param viewId
     * @param icon
     */
    protected void setIcon(int viewId, int icon) {
        View view = findViewById(viewId);
        if(view != null){
            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);
            view.setBackgroundResource(icon);
        }
    }

    /**
     * 设置点击
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId,View.OnClickListener listener){
        View view = findViewById(viewId);
        if(view != null)
            view.setOnClickListener(listener);
    }


    public <T extends View> T findViewById(int viewId){
        return (T)mNavigationView.findViewById(viewId);
    }

    /**
     * 绑定和创建View
     */
    private void createAndBindView() {
        // 1. 创建View

        if(mParams.mParent == null){
            // 获取activity的根布局，View源码
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
            LogUtils.e(TAG,mParams.mParent+"");
        }

        // 处理Activity的源码，后面再去看

        if(mParams.mParent == null){
            return;
        }

        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);// 插件换肤

        // 2.添加
        mParams.mParent.addView(mNavigationView, 0);

        applyView();
    }

    // Builder  仿照系统写的， 套路 AbsNavigationBar  Builder  参数Params
    public abstract static class Builder<D extends AbsNavigationBar> {

        public Builder(Context context, ViewGroup parent) {

        }

        public abstract D build();


        public static class AbsNavigationParams {
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
