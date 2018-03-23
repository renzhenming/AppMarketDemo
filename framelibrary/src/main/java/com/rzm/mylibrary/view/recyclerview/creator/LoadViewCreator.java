package com.rzm.mylibrary.view.recyclerview.creator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by renzhenming on 2018/3/17.
 * 上拉加载更多辅助类
 */

public abstract class LoadViewCreator {

    /**
     * 获取加载更多的View,这个View需要设置到RecyclerView中，所以inflate的时候需要parent，在这里回调过来
     * @param context
     * @param parent
     * @return
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     * @param currentDragHeight
     * @param dragViewHeight
     * @param currentLoadStatus
     */
    public abstract void onPull(int currentDragHeight,int dragViewHeight,int currentLoadStatus);

    /**
     * 正在加载
     */
    public abstract void onLoading();

    /**
     * 加载完成
     */
    public abstract void onStopLoad();
}
