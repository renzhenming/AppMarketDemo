package com.example.mylibrary.view.indicator.recyclerview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylibrary.R;

/**
 * Created by renzhenming on 2017/11/9.
 */

public class DefaultLoadCreator extends LoadViewCreator {
    private TextView mLoadMoreView;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_default_load_footer, parent, false);
        mLoadMoreView = refreshView.findViewById(R.id.load_tv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus) {
        mLoadMoreView.setText("上拉加载");
    }

    @Override
    public void onLoading() {
        mLoadMoreView.setText("正在加载");
    }

    @Override
    public void onStopLoad() {
    }
}
