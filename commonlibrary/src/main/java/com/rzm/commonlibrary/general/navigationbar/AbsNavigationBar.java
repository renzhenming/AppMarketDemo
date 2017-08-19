package com.rzm.commonlibrary.general.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by renzhenming on 2017/8/17.
 * NavigationBar 基类 供继承
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    public View mNavigationView;

    public AbsNavigationBar(P mParams) {
        this.mParams = mParams;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    /**
     * 创建和绑定view
     */
    private void createAndBindView() {

        //继承自AppCompatActivity
        if (mParams.mParent == null){
            //获取activity的根布局
            if (mParams.mContext instanceof AppCompatActivity){
                ViewGroup activityRoot = (ViewGroup) ((Activity) mParams.mContext).findViewById(android.R.id.content);
                mParams.mParent = (ViewGroup)activityRoot.getChildAt(0);
            }else{
                throw new IllegalArgumentException("application context can not be cast to Activity");
            }
        }

        //继承自Activity(获取根节点的方式是不一样的，看源码)
        //   ....

        if (mParams.mParent == null)
            return;

        //创建view
        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(), mParams.mParent,false);
        //添加
        mParams.mParent.addView(mNavigationView,0);
        //绑定参数
        applyView();
    }

    //Builder模式，基本的三个类  NavigationBar Builder Params
    public static abstract class Builder{

        AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent){
            P = new AbsNavigationParams(context,parent);
        }

        //需要一个创建的方法
        public abstract INavigationBar build();


        public static class AbsNavigationParams {
            public ViewGroup mParent;
            public Context mContext;

            public AbsNavigationParams(Context context, ViewGroup parent){
                this.mContext= context;
                this.mParent = parent;
            }
        }

    }
}
