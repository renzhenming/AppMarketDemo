package com.rzm.commonlibrary.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.utils.LogUtils;

/**
 * Created by renzhenming on 2018/3/28.
 */

public class SlidingView extends HorizontalScrollView{

    private final String TAG = getClass().getSimpleName();

    //侧滑菜单的宽度
    private final int mMenuWidth;

    private final Context mContext;

    //根布局，用作HorizontalScrollView的唯一子view
    //private final LinearLayout mRootView;

    //默认的侧边栏滑出最大位置距离右边屏幕的距离
    private float mMenuPaddingRight = 100;

    //侧边菜单布局
    private View mMenuView;

    //主页内容布局,包括用户的contentView和我们添加的阴影效果
    private ViewGroup mContentView;

    //侧边栏menu是否打开
    private boolean mMenuOpened;

    //设置抽屉模式
    private boolean mParallaxMode;

    //手势监听器
    private GestureDetector mGestureDetector;

    //快速滑动打开收起侧边栏的敏感指数，越大敏感度越小，约不容易打开关闭
    //不能过大也能过小，
    private float mSensitivity = 1000;

    //设置阴影
    private ImageView mShadowIv;

    //视差模式下，默认的侧边栏缩进的距离相对于侧边栏宽度的比例
    private float mTransitionPercent = 0.8f;

    //阴影透明度能达到的最深颜色值
    private String mAlphaColor="#99000000";

    public SlidingView(Context context) {
        this(context,null);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingView);
        mMenuPaddingRight = typedArray.getDimension(R.styleable.SlidingView_menuPaddingRight, dp2px((int) mMenuPaddingRight));
        mParallaxMode = typedArray.getBoolean(R.styleable.SlidingView_menuParallax, mParallaxMode);

        //侧边栏的宽度
        mMenuWidth = (int) (getScreenWidth() - mMenuPaddingRight);
        typedArray.recycle();

//        mRootView = new LinearLayout(context);
//        ViewGroup.LayoutParams rootLayoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        mRootView.setLayoutParams(rootLayoutParams);
//        mRootView.setOrientation(LinearLayout.HORIZONTAL);

//        addView(mRootView);

        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //如果需要GestureDetector处理，则由其处理
        if (mGestureDetector.onTouchEvent(ev)){
            return mGestureDetector.onTouchEvent(ev);
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                //这个位置指的是，当前这个view和手机屏幕左边届交点距离view最左侧的距离，这里最小为0，不会为负
                //当侧边栏滑出的过程中，scrollX呈递减的趋势，直到=0
                int scrollX = getScrollX();
                LogUtils.d(TAG,"scrollX:"+scrollX);
                if (scrollX > mMenuWidth/2){
                    //滑出了一部分，但是还没有到宽度的1／2
                    closeMenu();
                }else{
                    openMenu();
                }
                return false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开侧边栏
     */
    private void openMenu() {
        smoothScrollTo(0,0);
        mMenuOpened = true;
    }

    /**
     * 关闭侧边栏
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth,0);
        mMenuOpened = false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //默认设置侧滑菜单隐藏
        if (changed){
            //scrollTo 让当前屏幕左边界滑动到（mMenuWidth，0）这个坐标位置
            scrollTo(mMenuWidth, 0);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取根view
        ViewGroup rootView = (ViewGroup) getChildAt(0);

        int childCount = rootView.getChildCount();
        if (childCount > 2){
            throw new IllegalStateException("you should add two child view at most");
        }

        //获取两个布局
        mMenuView = rootView.getChildAt(0);

        //给内容添加阴影效果
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(contentParams);
        //获取原来的内容布局，并把原来的内容布局从LinearLayout中异常
        View oldContentView = rootView.getChildAt(1);
        rootView.removeView(oldContentView);

        //把原来的内容View 和 阴影加到我们新创建的内容布局中
        mContentView.addView(oldContentView);
        //创建阴影
        mShadowIv = new ImageView(mContext);
        mShadowIv.setBackgroundColor(Color.parseColor(mAlphaColor));
        mContentView.addView(mShadowIv);
        //把包含阴影的新的内容View 添加到 LinearLayout中
        rootView.addView(mContentView);

        //设置二者宽度
        mMenuView.getLayoutParams().width =  mMenuWidth;
        mContentView.getLayoutParams().width = getScreenWidth();
    }

    private float dp2px(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    /**
     * 屏幕宽度
     * @return
     */
    public int getScreenWidth(){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.widthPixels;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        // 处理手势快速滑动
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //velocityX，velocityY都是朝坐标系正方向滑动为正，负方向为负，表示正方向上单位时间的速度
            //velocityX为正，表示单位时间内，
            LogUtils.d(TAG,"velocityX:"+velocityX+",velocityY:"+velocityY);

            if (mMenuOpened){
                if (velocityX < -mSensitivity){
                    toggleMenu();
                    return true;
                }
            }else{
                if (velocityX>mSensitivity){
                    toggleMenu();
                    return true;
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    /**
     * 切换菜单的状态
     */
    private void toggleMenu() {
        if(mMenuOpened){
            closeMenu();
            LogUtils.d(TAG,"close");
        }else{
            openMenu();
            LogUtils.d(TAG,"open");
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // l 是 当前滚动的x距离 指的是当前view和屏幕左侧边界相交点距离view最左侧的距离
        LogUtils.e(TAG,"onScrollChanged：l===="+l);

        if (mParallaxMode) {
            //在onLayout中，默认会将这个SlidingView向左移动一个侧边栏宽度
            //这个时候onScrollChanged会被调用，这时候会执行这行代码，将侧边栏向x轴正方向移动一定比例的l宽度，实际上
            //也此时，侧边栏已经被放在了主也contentView的下边，此时，滑动contentView的时候，会达到一种视觉差异效果
            mMenuView.setTranslationX(l * mTransitionPercent);
        }
        //给内容添加阴影效果 - 计算梯度值
        float gradientValue = l * 1f / mMenuWidth;
        LogUtils.e(TAG,"gradientValue:"+gradientValue);

        //这是 1 - 0 变化的值
        //给内容添加阴影效果 - 给阴影的View指定透明度 0 - 1 变化的值
        float shadowAlpha = 1 - gradientValue;

        LogUtils.e(TAG,"shadowAlpha:"+shadowAlpha);
        mShadowIv.setAlpha(shadowAlpha);
    }

}
