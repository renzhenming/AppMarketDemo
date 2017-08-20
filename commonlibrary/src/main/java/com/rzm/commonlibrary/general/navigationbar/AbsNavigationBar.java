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
            if (mParams.mContext instanceof Activity){
                //这种方式存在一种问题，如果我们设置的跟布局是RelativeLayout会导致导航栏下边的view和导航栏部分重叠，
                //那么似乎也只能把我们的跟布局限制为LinearLayout但是这种方式明显很不通用，所以我们换一种方式处理，将下边两行注释掉
                //ViewGroup activityRoot = (ViewGroup) ((Activity) mParams.mContext).findViewById(android.R.id.content);
                //mParams.mParent = (ViewGroup)activityRoot.getChildAt(0);
                //通过看源码我们知道DecorView是一个FrameLayout，是系统的一个ViewGroup，DecorView.getChildAt(0)位置得到的是一个LinearLayout
                // 这样一来，我们的导航栏就跟我们自己的布局没有关系了，是位于凌驾于我们布局之上的系统布局中

                //这样设置之后，发现导航栏和状态栏重叠，所以在导航栏布局中设置了一个MarginTop,防止交叉
                ViewGroup activityRoot = (ViewGroup) ((Activity) mParams.mContext).getWindow().getDecorView();
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
