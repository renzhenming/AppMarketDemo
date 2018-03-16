package com.example.mylibrary.view.recyclerview2.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by renzhenming on 2018/3/16.
 *
 * 1.添加头部和尾部
 * 2.设置多种item
 */

public class WrapRecyclerView extends RecyclerView {

    //列表数据adapter
    private Adapter mAdapter;

    //包装类adapter
    private WrapRecyclerAdapter mWrapRecyclerAdapter;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {

        if (mAdapter != null){
            //防止重复设置adapter
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;

        if (mAdapter instanceof WrapRecyclerAdapter){
            mWrapRecyclerAdapter = (WrapRecyclerAdapter)mAdapter;
        }else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(mAdapter);
        }
        //将使用WrapRecyclerAdapter包裹一层之后的adapter设置
        super.setAdapter(mWrapRecyclerAdapter);

        //注册观察者(注意这里是列表数据的adapter设置的观察者，如果用包装类adapter设置是无效的，当列表数据adapter刷新的时候，
        // mWrapRecyclerAdapter也跟着一块刷新)
        mAdapter.registerAdapterDataObserver(mDataObserver);

        //解决LayoutManger为GridLayoutManager时添加头部宽度无法沾满一行的问题
        mWrapRecyclerAdapter.adjustSpanSize(this);

    }

    /**
     * 添加头部
     *
     * 仿照listView源码编写，listView也是内部通过adapter设置的
     * @param view
     */
    public void addHeaderView(View view){
        if (mWrapRecyclerAdapter == null){
            throw new NullPointerException("adapter cannot be null");
        }

        mWrapRecyclerAdapter.addHeaderView(view);
    }

    /**
     * 添加尾部
     * @param view
     */
    public void addFooterView(View view){
        if (mWrapRecyclerAdapter == null){
            throw new NullPointerException("adapter cannot be null");
        }

        mWrapRecyclerAdapter.addFooterView(view);
    }

    /**
     * 移除头部
     * @param view
     */
    public void removeHeaderView(View view){
        if (mWrapRecyclerAdapter == null){
            throw new NullPointerException("adapter cannot be null");
        }

        mWrapRecyclerAdapter.removeHeaderView(view);
    }

    /**
     * 移除尾部
     * @param view
     */
    public void removeFooterView(View view){
        if (mWrapRecyclerAdapter == null){
            throw new NullPointerException("adapter cannot be null");
        }

        mWrapRecyclerAdapter.removeFooterView(view);
    }

    /**
     * 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
     */
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart,itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyItemRangeChanged(positionStart,itemCount,payload);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyItemRangeInserted(positionStart,itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyItemRangeRemoved(positionStart,itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null)return;
            if (mWrapRecyclerAdapter != mAdapter){
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition,toPosition);
            }
        }
    };
}
