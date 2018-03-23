package com.rzm.mylibrary.view.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p/>
 * Created by 曾辉 on 2016/11/18.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class BannerViewPager extends ViewPager {

    private static final String TAG = "BannerViewPager";

    // 1.字定义 BannerViewPager - 自定义的Adapter
    private BannerAdapter mAdapter;

    // 2.实现自动轮播 - 发送消息的msgWhat
    private final int SCROLL_MSG = 0x0011;

    // 2.实现自动轮播 - 页面切换间隔时间
    private int mCutDownTime = 3500;

    // 3.改变ViewPager切换的速率 - 自定义的页面切换的Scroller
    private BannerScroller mScroller;

    // 2.实现自动轮播 - 发送消息Handler
    private Handler mHandler;

    // 10.内存优化 --> 当前Activity
    private Activity mActivity;
    // 10.内存优化 --> 复用的View
    private List<View> mConvertViews;

    // 是否可以滚动
    private boolean mScrollAble = true;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mActivity = (Activity) context;

        try {
            // 3.改变ViewPager切换的速率
            // 3.1 duration 持续的时间  局部变量
            // 3.2.改变 mScroller private 通过反射设置
            Field field = ViewPager.class.getDeclaredField("mScroller");
            // 设置参数  第一个object当前属性在哪个类  第二个参数代表要设置的值
            mScroller = new BannerScroller(context);
            // 设置为强制改变private
            field.setAccessible(true);
            field.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mConvertViews = new ArrayList<>();

        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 每隔*s后切换到下一页
                setCurrentItem(getCurrentItem() + 1,true);
                // 不断循环执行
                startRoll();
            }
        };
    }

    /**
     * 3.设置切换页面动画持续的时间
     */
    public void setScrollerDuration(int scrollerDuration) {
        mScroller.setScrollerDuration(scrollerDuration);
    }

    /**
     * 1.设置自定义的BannerAdapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        // 设置父类 ViewPager的adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 2.实现自动轮播
     */
    public void startRoll() {
        // adapter不能是空
        if (mAdapter == null) {
            return;
        }

        // 判断是不是只有一条数据
        mScrollAble = mAdapter.getCount() != 1;

        if (mScrollAble && mHandler != null) {
            // 清除消息
            mHandler.removeMessages(SCROLL_MSG);
            // 消息  延迟时间  让用户自定义  有一个默认  3500
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
        }
    }

    /**
     * 2.销毁Handler停止发送  解决内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            // 解除绑定
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }


    /** rzm add
     * *********************************************************************************************
     *
     * 在RecyclerView中使用ViewPager时，会出现两个诡异的bug：
     *
     *    1.RecyclerView滚动上去，直至ViewPager看不见，再滚动下来，ViewPager下一次切换没有动画
     *
     *    2.初次加载显示页面的时候，如果已经设置了viewpager到recyclerview中，从服务器拿到数据后进行刷新，notify，也会导致
     *    同样的问题，viewpager第一次切换没有动画
     *
     * 原因:
     *
     * ViewPager里有一个私有变量mFirstLayout，它是表示是不是第一次显示布局，如果是true，则使用无动画的方式显示当前item，
     * 如果是false，则使用动画方式显示当前item。
     *
     * 源码如下:
     *
     * void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
     *        ...
     *
     * if (mFirstLayout) {
     *        // We don't have any idea how big we are yet and shouldn't have any pages either.
     *        // Just set things up and let the pending layout handle things.
     *        mCurItem = item;
     *       if (dispatchSelected) {
     *          dispatchOnPageSelected(item);
     *       }
     *       requestLayout();
     * } else {
     *       populate(item);
     *       scrollToItem(item, smoothScroll, velocity, dispatchSelected);
     *       ...
     * }
     *
     * 当ViewPager滚动上去后，因为RecyclerView的回收机制，ViewPager会走onDetachFromWindow，当再次滚动下来时，
     * ViewPager会走onAttachedToWindow，而问题就出在onAttachToWindow。
     *
     * @Override
     *  protected void onAttachedToWindow() {
     *      super.onAttachedToWindow();
     *      mFirstLayout = true;
     *  }
     *
     * 解决办法：
     *
     * 重写onAttachedToWindow方法，把mFirstLayout再重置成false，因为mFirstLayout是private变量，我们不能直接访问，
     * 所以只能反射了。
     *
     * *********************************************************************************************
     */
    @Override
    protected void onAttachedToWindow() {
        if (mAdapter != null) {
            initHandler();
            startRoll();
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        super.onAttachedToWindow();
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this,false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给ViewPager设置适配器
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // 为了实现无线循环
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 官方推荐这么写  源码
            return view == object;
        }

        /**
         * 创建ViewPager条目回调的方法
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // Adapter 设计模式为了完全让用户自定义
            // position  0 -> 2的31次方
            View bannerItemView = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            // 添加ViewPager里面
            container.addView(bannerItemView);
            bannerItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 回调点击监听
                    if (mListener != null) {
                        mListener.click(position % mAdapter.getCount());
                    }
                }
            });
            return bannerItemView;
        }

        /**
         * 销毁条目回调的方法
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    /**
     * 10.获取复用界面
     */
    public View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    /**
     * 10.设置点击回调监听
     */
    private BannerItemClickListener mListener;

    public void setOnBannerItemClickListener(BannerItemClickListener listener) {
        this.mListener = listener;
    }

    // 10.优化思想 点击回调监听
    public interface BannerItemClickListener {
        public void click(int position);
    }

    // 管理Activity的生命周期
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks =
            new DefaultActivityLifecycleCallbacks() {
                @Override
                public void onActivityResumed(Activity activity) {
                    // 是不是监听的当前Activity的生命周期
                    // Log.e("TAG", "activity --> " + activity + "  context-->" + getContext());
                    if (activity == mActivity) {
                        // 开启轮播
                        startRoll();
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    if (activity == mActivity) {
                        // 停止轮播
                        mHandler.removeMessages(SCROLL_MSG);
                    }
                }
            };
}
