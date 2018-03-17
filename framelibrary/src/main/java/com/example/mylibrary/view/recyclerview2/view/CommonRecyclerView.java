package com.example.mylibrary.view.recyclerview2.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.mylibrary.view.recyclerview2.creator.LoadViewCreator;

/**
 * Created by renzhenming on 2018/3/17.
 * 1.添加头部和尾部
 * 2.设置多种item
 * 3.下拉刷新
 * 4.上拉加载下一页
 */

public class CommonRecyclerView extends RefreshRecyclerView{
    //上拉加载更多辅助类
    private LoadViewCreator mLoadViewCreator;

    //加载更多的view布局
    private View mLoadView;

    private float mDownY;

    //加载更多布局的高度
    private int mLoadViewHeight;

    //当前的状态
    private int mCurrentLoadStatus;

    // 默认状态
    public int LOAD_STATUS_NORMAL = 0x0055;

    // 上拉加载更多状态
    public static int LOAD_STATUS_PULL_DOWN = 0x0066;

    // 松开加载更多状态
    public static int LOAD_STATUS_LOOSEN_LOADING = 0x0077;

    // 正在加载更多状态
    public int LOAD_STATUS_LOADING = 0x0088;

    //当前是否在拖拽
    private boolean mCurrentDrag = false;

    public CommonRecyclerView(Context context) {
        super(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 先处理上拉加载更多，同时考虑加载列表的不同风格样式，确保这个项目还是下一个项目都能用
     * 所以我们不能直接添加View，需要利用辅助类
     * @param creator
     */
    public void addLoadViewCreator(LoadViewCreator creator){
        this.mLoadViewCreator = creator;
        addLoadView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addLoadView();
    }

    /**
     * 添加加载更多的view
     */
    private void addLoadView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mLoadViewCreator != null){
            View loadView = mLoadViewCreator.getLoadView(getContext(), this);
            if (loadView != null){
                addFooterView(loadView);
                this.mLoadView = loadView;
            }
        }
    }

    /**
     * 在dispatch中处理按下和抬起的事件，因为recyclerView如果已经设置了条目点击事件，那么在onTouchEvent中，按下的事件
     * 不会被处理
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载更多状态
     */
    private void restoreLoadView() {
        //获取当前位置loadView的marginBottom值
        if (mLoadView == null)return;

        int currentBottomMargin = ((MarginLayoutParams) (mLoadView.getLayoutParams())).bottomMargin;

        //最终要滑动到marginBottom为0的位置
        int finalBottomMargin = 0;

        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING){
            mCurrentLoadStatus = LOAD_STATUS_LOADING;

            if (mLoadViewCreator != null){
                mLoadViewCreator.onLoading();
            }

            if (mListener != null) {
                mListener.onLoad();
            }
        }

        //distance用作动画执行的时间
        int distance = currentBottomMargin - finalBottomMargin;

        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin).setDuration(distance);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {

                float currentTopMargin = (float) animation.getAnimatedValue();
                setLoadViewMarginBottom((int) currentTopMargin);

            }
        });

        animator.start();

        mCurrentDrag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING){
                    // 如果没有到达最底端，也就是说还可以向下滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

                if (mLoadViewCreator != null){
                    mLoadViewHeight = mLoadView.getMeasuredHeight();
                }

                // 解决上拉加载更多自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(getAdapter().getItemCount() - 1);
                }

                //获取手指触摸拖拽的距离
                int distanceY = (int) (e.getRawY() - mDownY);
                distanceY = (int) (distanceY * mDragResistance);

                // 如果是已经到达底部，并且不断的拉动，那么不断的改变LoadView的marginBottom的值

                //注意上拉得到的distanceY是负值
                if (distanceY < 0){

                    //通过设置loadView的marginBottom值不断的移动loadView的位置
                    setLoadViewMarginBottom(-distanceY);

                    //更新状态
                    updateLoadStatus(-distanceY);

                    //当前是否在拖拽
                    mCurrentDrag = true;

                    return true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 回调状态
     * @param dragHeight
     */
    private void updateLoadStatus(int dragHeight) {
        if (dragHeight <= 0){
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        }else if (dragHeight < mLoadViewHeight){
            //当拖动距离小于loadView的高度的时候
            mCurrentLoadStatus = LOAD_STATUS_PULL_DOWN;
        }else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if (mLoadViewCreator != null){
            mLoadViewCreator.onPull(dragHeight,mLoadViewHeight,mCurrentLoadStatus);
        }
    }

    /**
     * 设置loadView的marginBottom值
     * @param marginBottom
     */
    private void setLoadViewMarginBottom(int marginBottom) {
        if (mLoadView == null) return;
        MarginLayoutParams layoutParams = (MarginLayoutParams) mLoadView.getLayoutParams();
        if (marginBottom < 0){
            marginBottom = 0;
        }
        layoutParams.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(layoutParams);
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     *
     * 如下方法已过时:
     *
     * if (android.os.Build.VERSION.SDK_INT < 14) {
     *    return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
     * } else {
     *    return ViewCompat.canScrollVertically(this, -1);
     * }
     *
     * 方法 canScrollVertically(int direction):
     *
     * Check if this view can be scrolled vertically in a certain direction.
     * params Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    private boolean canScrollDown() {
        return canScrollVertically(1);
    }


    public void stopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if (mLoadViewCreator != null) {
            mLoadViewCreator.onStopLoad();
        }
    }


    // 处理加载更多回调监听
    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }
}


























