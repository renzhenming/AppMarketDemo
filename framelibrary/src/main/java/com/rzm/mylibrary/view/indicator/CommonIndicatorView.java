package com.rzm.mylibrary.view.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.rzm.mylibrary.R;
import com.rzm.commonlibrary.utils.LogUtils;

/**
 * Created by renzhenming on 2018/3/9.
 * <p>
 * 仿照TableLayout效果实现的view pager指示器
 */

public class CommonIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private static final String TAG = CommonIndicatorView.class.getSimpleName();

    private CommonIndicatorGroupView mIndicatorContainer;
    private CommonIndicatorAdapter mAdapter;
    private ViewPager mViewPager;

    // 获取一屏显示多少个Item,默认是0
    private int mTabVisibleNums = 0;
    private int mItemWidth;
    private int mCurrentPosition = 0;
    private boolean mExecuteScroll = false;


    public CommonIndicatorView(Context context) {
        this(context, null);
    }

    public CommonIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CommonIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //HorizontalScrollView中放置一个LinearLayout来添加item

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonIndicatorView);
        mTabVisibleNums = typedArray.getInt(R.styleable.CommonIndicatorView_tabVisibleNum, mTabVisibleNums);
        typedArray.recycle();

        mIndicatorContainer = new CommonIndicatorGroupView(context);
        addView(mIndicatorContainer);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //指定item宽度
            mItemWidth = getItemWidth();
            int count = mAdapter.getCount();
            //给每一个item设置这个宽度
            for (int i = 0; i < count; i++) {
                View view = mIndicatorContainer.getItemAt(i);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = mItemWidth;

                //不调用setLayoutParams，动态宽度不会生效
                view.setLayoutParams(layoutParams);
            }
            LogUtils.e(TAG, "CommonIndicatorView mItemWidth -> " + mItemWidth);

            //添加底部跟踪指示器
            mIndicatorContainer.addBottomTrackView(mAdapter.getBottomTrackView(), mItemWidth);
        }
    }

    //获取每个item的宽度
    private int getItemWidth() {
        int itemWidth = 0;
        //获取当前控件的宽度
        int width = getWidth();
        if (mTabVisibleNums != 0) {
            //如果指定了tab的个数，就将整个控件的宽度平均分，得到每个item的宽度返回
            itemWidth = width / mTabVisibleNums;
            return itemWidth;
        }

        // 如果没有指定获取最宽的一个作为ItemWidth
        int maxItemWidth = 0;

        //所有item的总宽度
        int allItemWidth = 0;
        int itemCounts = mAdapter.getCount();
        for (int i = 0; i < itemCounts; i++) {
            View v = mIndicatorContainer.getItemAt(i);
            int childWidth = v.getMeasuredWidth();
            maxItemWidth = Math.max(maxItemWidth, childWidth);
            allItemWidth += childWidth;
        }

        itemWidth = maxItemWidth;
        //如果所有item的总宽度不足一屏的宽度，则设置铺满一屏
        if (allItemWidth < width) {
            itemWidth = width / itemCounts;
        }

        return itemWidth;
    }

    public void setAdapter(CommonIndicatorAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("Adapter cannot be null!");
        }
        this.mAdapter = adapter;
        //获取item数目
        int count = mAdapter.getCount();
        //动态添加到布局
        for (int i = 0; i < count; i++) {
            View indicatorView = mAdapter.getView(i, mIndicatorContainer);
            mIndicatorContainer.addIndicatorView(indicatorView);
            onIndicatorClick(indicatorView, i);
        }

        //默认点亮第一个item tab
        mAdapter.highLightIndicator(mIndicatorContainer.getItemAt(0));

    }

    /**
     * 重载一个setAdapter的方法
     *
     * @param adapter   适配器
     * @param viewPager 联动的ViewPager
     *                  <p>
     *                  需要实现的效果：
     *                  1. 当ViewPager滚动的时候头部需要自动将当前Item滚动到最中心；
     *                  2. 点击Item之后ViewPager能够切换到对应的页面；
     *                  3. 需要页面切换之后需要回调，让用户切换当前选中的状态，需要在Adapter中增加方法；
     *                  4. 有些效果需要加入指示器，但并不是每种效果都需要
     */
    public void setAdapter(CommonIndicatorAdapter adapter, ViewPager viewPager) {
        setAdapter(adapter);

        // 为ViewPager添加滚动监听事件
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    private void onIndicatorClick(View indicatorView, final int i) {
        indicatorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(i);
                    //切换了viewpager之后，我们要同时将对应的indicator也移动到中间位置
                    indicatorSmoothScrollTo(i);
                    //移动下边的track
                    mIndicatorContainer.scrollBottomTrack(i);


                    //但是其实不必要非要重新移动，之前的viewpager的监听，当viewpager切换时，在onPageScrolled
                    //中会同时将indicator做移动
                    //所以这里有两种处理方式，第一就是当点击的时候，不执行onPageScrolled的代码，重新移动indicator
                    //第二就是直接在回调中移动indicator

                    //ps 后来证明，直接在回调中执行是有bug的
                }
            }
        });
    }

    /**
     * 滚动到当前的位置带动画
     */
    private void indicatorSmoothScrollTo(int position) {
        // 当前的偏移量
        int currentOffset = ((position) * mItemWidth);
        // 原始的左边的偏移量
        int originLeftOffset = (getWidth() - mItemWidth) / 2;
        // 当前应该滚动的位置
        int scrollToOffset = currentOffset - originLeftOffset;
        // smoothScrollTo
        smoothScrollTo(scrollToOffset, 0);
    }

    /**
     * @param position             当前位置
     * @param positionOffset       当前偏移百分比
     * @param positionOffsetPixels 当前偏移的像素总数
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mExecuteScroll) {
            // 在ViewPager滚动的时候会不断的调用该方法
            LogUtils.e(TAG, "position --> " + position + " positionOffset --> " + positionOffset + ",positionOffsetPixels->" + positionOffsetPixels);
            // 在不断滚动的时候让头部的当前Item一直保持在最中心
            scrollItemToPosition(position, positionOffset);

            //滚动底部的track view
            mIndicatorContainer.scrollBottomTrack(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

        //当页面切换时，获取到切换之前的那个页面tab，重置它的状态
        View lastView = mIndicatorContainer.getItemAt(mCurrentPosition);
        mAdapter.restoreIndicator(lastView);

        //高亮当前选中的tab
        mCurrentPosition = position;
        highLightIndicator(mCurrentPosition);
    }

    /**
     * 当点击item切换viewpager的时候，viewpager的状态变化时不正常的
     * 从按下到弹起的过程中，state状态由2变道0，而正常滑动切换应该是
     * 1-->2-->0，所以点击切换在回调到onPageScroll方法中切换track指示器的时候
     * 会出现卡顿的情况，所以这就是为什么点击切换的时候，indicator的切换我们要
     * 禁用掉onPageScroll方法中的代码，自己另外去写代码滑动指示器的原因
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        LogUtils.e(TAG, "state --> " + state);
        if (state == 1) {
            mExecuteScroll = true;
        }
        if (state == 0) {
            mExecuteScroll = false;
        }
    }

    private void highLightIndicator(int position) {
        View currentView = mIndicatorContainer.getItemAt(position);
        mAdapter.highLightIndicator(currentView);
    }

    private void scrollItemToPosition(int position, float positionOffset) {
        //随着viewpager的滑动，item当前相应的偏移量
        int currentOffSet = (int) ((position + positionOffset) * mItemWidth);

        LogUtils.e(TAG, "currentOffSet --> " + currentOffSet);

        // 原始的左边的偏移量
        int originLeftOffset = (getWidth() - mItemWidth) / 2;

        LogUtils.e(TAG, "originLeftOffset --> " + originLeftOffset);

        // 当前应该滚动的位置(这样做的目的是，当滑动viewpager切换页面后，让与之对应的item位于屏幕的中间)
        int scrollToOffset = currentOffSet - originLeftOffset;

        LogUtils.e(TAG, "scrollToOffset --> " + scrollToOffset);

        if (scrollToOffset < 0)
            scrollToOffset = 0;
        //注意scrollTo是滑动到某一位置，和滑动多少距离的scrollBy的区别
        //滑动到某一位置，指的是屏幕左边界和布局相交的位置坐标
        scrollTo(scrollToOffset, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

}
