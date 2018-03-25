package com.rzm.commonlibrary.views.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by renzhenming on 2018/3/10.
 *
 * indicator 的容器，包含indicator和底部指示器
 */

public class CommonIndicatorGroupView extends FrameLayout {
    private final LinearLayout mIndicatorGroup;
    private View mBottomTrackView;
    private int mItemWidth;
    private LayoutParams mTrackLayoutParams;
    //track view距离左边的长度,当底部的track指示器宽度较小的时候，小于item的宽度，此时
    //设置它在中间位置，所以它距离这个item最左边是有一段距离的，这个距离就是mInitLeftMargin
    private int mInitLeftMargin;

    public CommonIndicatorGroupView(@NonNull Context context) {
        this(context,null);
    }

    public CommonIndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CommonIndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化指示器条目的容器
        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);
    }

    //获取指定位置的tab
    public View getItemAt(int position) {
        return mIndicatorGroup.getChildAt(position);
    }

    //添加indicator view
    public void addIndicatorView(View indicatorView) {
        mIndicatorGroup.addView(indicatorView);
    }

    //底部指示器
    public void addBottomTrackView(View bottomTrackView, int itemWidth) {
        if (bottomTrackView == null)return;

        this.mItemWidth = itemWidth;

        this.mBottomTrackView = bottomTrackView;
        addView(mBottomTrackView);

        //设置底部位置和宽度
        mTrackLayoutParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackLayoutParams.gravity = Gravity.BOTTOM;

        //如果用户设置了宽度，就使用用户设置的，如果用户没有设置，就设置为item的宽度
        int trackWidth = mTrackLayoutParams.width;

        //没有设置宽度
        if (mTrackLayoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT){
            trackWidth = mItemWidth;
        }

        //设置的宽度超过item宽度
        if (trackWidth > mItemWidth){
            trackWidth = mItemWidth;
        }

        mTrackLayoutParams.width = trackWidth;

        //当宽度小与item宽度的时候，确保它在中间位置
        mInitLeftMargin = (mItemWidth-trackWidth)/2;
        mTrackLayoutParams.leftMargin = mInitLeftMargin;
    }

    /**
     * 滚动底部的指示器，设置leftMargin
     * @param position
     * @param positionOffset
     */
    public void scrollBottomTrack(int position, float positionOffset) {
        if (mBottomTrackView == null)
            return;
        int leftMargin = (int) ((position+positionOffset)*mItemWidth);
        mTrackLayoutParams.leftMargin = leftMargin+mInitLeftMargin;
        mBottomTrackView.setLayoutParams(mTrackLayoutParams);
    }

    /**
     * 点击移动指示器
     * @param position
     */
    public void scrollBottomTrack(int position) {
        if (mBottomTrackView == null)
            return;
        //最终要移动到的位置
        int finalLeftMargin = (int)(position*mItemWidth)+mInitLeftMargin;
        //初始位置，也就是当前位置
        int currentLeftMargin = mTrackLayoutParams.leftMargin;
        //移动的距离
        int distance = finalLeftMargin-currentLeftMargin;
        //动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin);
        animator.setDuration((long) (Math.abs(distance)*0.4));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float leftMargin = (float) animation.getAnimatedValue();
                mTrackLayoutParams.leftMargin = (int) leftMargin;
                mBottomTrackView.setLayoutParams(mTrackLayoutParams);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
