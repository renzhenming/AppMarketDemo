package com.example.mylibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.example.mylibrary.skin.SkinAttrSupport;
import com.example.mylibrary.skin.SkinManager;
import com.example.mylibrary.skin.attr.SkinAttr;
import com.example.mylibrary.skin.attr.SkinView;
import com.example.mylibrary.skin.support.SkinAppCompatViewInflater;
import com.rzm.commonlibrary.stack.mvc.BaseActivity;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renzhenming on 2017/8/8.
 * 为后期的插件换肤预留的一个类,项目中activity通过继承这个中间类来继承BaseActivity
 * tip : 永远在activity和BaseActivity中预留一层，用于未来迭代可能存在的扩展
 */

public abstract class BaseSkinActivity extends BaseActivity {

    private static final java.lang.String TAG = "BaseSkinActivity";
    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (layoutInflater.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        }
        super.onCreate(savedInstanceState);

    }

    /**
     * From {@link LayoutInflater.Factory2}.
     */
    @Override
    public final View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);
        //一个activity布局对应多个SkinView
        if (view != null){
            SkinManager.getInstance().init(this);
            List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(context,attrs);
            SkinView skinView = new SkinView(view,skinAttrList);
            //交给Manager处理
            manageSkinView(skinView);
        }

        LogUtils.e(TAG,view+"");
        return view;
    }

    /**
     * 统一管理SkinView
     * @param skinView
     */
    private void manageSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true ,/* Read read app:theme as a fallback at all times for legacy reasons */
                true
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
