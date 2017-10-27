package com.rzm.commonlibrary.general.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by renzhenming on 2017/8/14.
 */

class CommonViewHelper {

    private View mContentView;
    //保存id，减少findviewbyid的次数
    private SparseArray<WeakReference<View>> mViewArray = new SparseArray<>();

    public CommonViewHelper(Context mContext, int mViewLayoutResId) {
        mContentView = LayoutInflater.from(mContext).inflate(mViewLayoutResId, null);
    }

    public CommonViewHelper() {

    }

    public void setContentView(View mView) {
        this.mContentView = mView;
    }

    /**
     * 设置文本
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        TextView textView = getView(viewId);
        if (textView != null)
            textView.setText(charSequence);
    }

    /**
     * 点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnClickListener(listener);

    }

    /**
     * 获取view,先获取缓存，没有再findViewById
     * @param viewId
     * @return
     */
    public <T extends View>T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViewArray.get(viewId);
        View view = null;
        if (viewWeakReference != null){
            view = viewWeakReference.get();
        }
        if (view == null){
            view = mContentView.findViewById(viewId);
            if (view != null){
                mViewArray.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setVisibility(int viewId, Integer integer) {
        View view = getView(viewId);
        if (view != null){
            view.setVisibility(integer);
        }
    }
}
