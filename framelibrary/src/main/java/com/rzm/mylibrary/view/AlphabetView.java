package com.rzm.mylibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.rzm.mylibrary.R;

/**
 * Created by renzhenming on 2018/3/24.
 * 实现根据字母分类排序的列表，多用于地址选择或者手机通讯录
 */

public class AlphabetView extends View{

    //普通模式绘制的画笔
    private final Paint mPaint;

    //触摸时绘制用的画笔
    private final Paint mTouchPaint;

    //默认字母大小
    private int mTextSize = 16;

    //默认字母颜色
    private int mTextColor = Color.BLACK;

    //触摸时字母颜色
    private int mTouchTextColor = Color.RED;

    //控件的高度
    private int mViewHeight;

    //26个字母
    private String mLetters[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N"
        ,"O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private int mItemHeight;

    //当前触摸的字母
    private String mCurrentTouchLetter;

    //当前是否在触摸
    private boolean mCurrentIsTouch = false;

    //抬起之后是否清除当前选中的高亮颜色
    private boolean mClearAfterUp = true;

    public AlphabetView(Context context) {
        this(context,null);
    }

    public AlphabetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public AlphabetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlphabetView);
        mTextColor = typedArray.getColor(R.styleable.AlphabetView_textColor, mTextColor);
        mTouchTextColor = typedArray.getColor(R.styleable.AlphabetView_touchTextColor, mTouchTextColor);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.AlphabetView_textSize, mTextSize);
        typedArray.recycle();

        //设置画笔用于绘制字母

        /**
         * 为什么使用两个画笔而不是单独的使用一个然后切换颜色的设置，因为paint是有一个
         * 设置颜色的方法的setColor,我们看一下setColor的源码，发现是native方法实现的，
         *  private static native void nSetColor(long paintPtr, @ColorInt int color);
         * 如果在onDraw中频繁的切换颜色性能会降低，所以这就是通常使用几种颜色定义几个画笔
         * 的原因
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(sp2px(mTextSize));

        mTouchPaint = new Paint();
        mTouchPaint.setAntiAlias(true);
        mTouchPaint.setDither(true);
        mTouchPaint.setColor(mTouchTextColor);
        mTouchPaint.setTextSize(sp2px(mTextSize));
    }

    private float sp2px(int sp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据布局中设置的宽度模式确定宽度
        int width =0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (mode == MeasureSpec.AT_MOST){
            //计算宽度,两边的padding加上文字宽度，这个文字随意，不过最好是26个字母中最宽的
            float textWidth = mPaint.measureText("");
            width = (int) (getPaddingLeft() + getPaddingRight()+textWidth);
        }
        //获取设置的高度
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取每个字母占的高度
        mItemHeight = (getHeight()-getPaddingTop() - getPaddingBottom())/mLetters.length;

        //绘制26个字母
        for (int i = 0; i < mLetters.length; i++) {
            //得到每个字母的中心位置,防止给布局设置了paddingTop，所以这里+上
            int letterCenterY = i* mItemHeight + mItemHeight /2+getPaddingTop();

            //得到每个字母的中心位置
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int dy = (int) ((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);

            //根据字母中心位置求字母的基线
            //baseline = center + (FontMetrics.bottom - FontMetrics.top)/2 - FontMetrics.bottom;
            int baseLine = letterCenterY+dy;

            //获取每一个字母绘制的x坐标
            float letterWidth = mPaint.measureText(mLetters[i]);
            //确保每一个字母都在中间位置
            int x = (int) (getWidth()/2 - letterWidth/2);
            //开始绘制
            /**
             * text 需要绘制的文字
             x 绘制文字原点X坐标
             y 绘制文字原点Y坐标
             xy指的时文字左下角的坐标，也就是基线和文字最左边下方的交点
             paint 画笔
             */
            if (mLetters[i].equals(mCurrentTouchLetter)){
                canvas.drawText(mLetters[i],x,baseLine,mTouchPaint);
            }else{
                canvas.drawText(mLetters[i],x,baseLine,mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                //当前触摸的位置,相对与控件的位置而不是相对于屏幕
                float startY = event.getY();
                //计算当前字母的位置
                int position = (int)startY/mItemHeight;

                //防止当触摸超出控件范围的时候，设定position值，防止数组越界
                if (position <0){
                    position = 0;
                }
                if (position > mLetters.length - 1){
                    position = mLetters.length - 1;
                }
                //滑动过程中如果滑动的位置和上一次相同，就不再处理，减少invalidate的次数
                if (mLetters[position].equals(mCurrentTouchLetter)){
                    return true;
                }
                //记录当前触摸的弥足
                mCurrentTouchLetter = mLetters[position];
                mCurrentIsTouch = true;
                if (mTouchListener != null){
                    mTouchListener.onTouch(mCurrentTouchLetter,mCurrentIsTouch);
                }

                break;

            case MotionEvent.ACTION_UP:
                //抬起时设置为false
                mCurrentIsTouch = false;
                if (mTouchListener != null){
                    mTouchListener.onTouch(mCurrentTouchLetter,mCurrentIsTouch);
                }
                if (mClearAfterUp){
                    //将当前触摸的字母设置为null,up之后重绘界面，清除高亮状态
                    mCurrentTouchLetter = null;
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 设置手指触摸抬起之后，是否恢复高亮状态为正常状态，默认为true
     * @param mClearAfterUp
     */
    public void setClearAfterUp(boolean mClearAfterUp) {
        this.mClearAfterUp = mClearAfterUp;
    }

    // 设置触摸监听
    private OnAlphabetTouchListener mTouchListener;
    public void setOnAlphabetTouchListener(OnAlphabetTouchListener touchListener) {
        this.mTouchListener = touchListener;
    }
    public interface OnAlphabetTouchListener {
        void onTouch(String letter, boolean isTouch);
    }
}





















