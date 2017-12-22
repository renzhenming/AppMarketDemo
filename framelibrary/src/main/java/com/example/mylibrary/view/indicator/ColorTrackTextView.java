package com.example.mylibrary.view.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.mylibrary.R;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2016/12/11.
 * Version 1.0
 * Description:  颜色跟踪的TextView
 */
public class ColorTrackTextView extends TextView {

    // 1. 实现一个文字两种颜色 - 绘制不变色字体的画笔
    private Paint mOriginPaint;
    // 1. 实现一个文字两种颜色 - 绘制变色字体的画笔
    private Paint mChangePaint;
    // 1. 实现一个文字两种颜色 - 当前的进度
    private float mCurrentProgress = 0f;

    // 2. 实现两种朝向 - 当前的朝向  从左到右还是从右到左
    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    /**
     * 2. 实现两种朝向 - 朝向的枚举类型
     */
    public enum Direction{
        RIGHT_TO_LEFT,LEFT_TO_RIGHT
    }

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    /**
     * 1.1 初始化画笔
     */
    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor  = array.getColor(R.styleable.ColorTrackTextView_originColor, getTextColors().getDefaultColor());
        int changeColor  = array.getColor(R.styleable.ColorTrackTextView_changeColor,getTextColors().getDefaultColor());

        mOriginPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        // 回收
        array.recycle();
    }

    /**
     * 1.根据颜色获取画笔
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        // 设置颜色
        paint.setColor(color);
        // 设置抗锯齿
        paint.setAntiAlias(true);
        // 防抖动
        paint.setDither(true);
        // 设置字体的大小  就是TextView的字体大小
        paint.setTextSize(getTextSize());
        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 不能使用系统的
        // super.onDraw(canvas);

        // 1.2 实现不同的颜色
        // 1.2.1 计算中间的位置  = 当前的进度 * 控件的宽度
        int width = getWidth();
        int middle = (int) (mCurrentProgress * width);
        // 1.2.3 根据中的位置去绘制  两边不同的文字颜色  截取绘制文字的范围
        String text = getText().toString();

        if (TextUtils.isEmpty(text)) return;
        // 绘制不变色的部分
        drawOriginText(canvas, text, middle);
        // 绘制变色的部分
        drawChangeText(canvas, text, middle);
    }

    /**
     * 2. 绘制变色的部分
     */
    private void drawChangeText(Canvas canvas, String text, int middle) {
        // 判断当前的朝向
        if(mDirection == Direction.LEFT_TO_RIGHT){
            drawText(text, canvas, mChangePaint, 0, middle);
        }else{
            drawText(text, canvas, mChangePaint, getWidth() - middle, getWidth());
        }
    }

    /**
     * 2. 绘制不变色的部分
     */
    private void drawOriginText(Canvas canvas,String text, int middle) {
        if(mDirection == Direction.LEFT_TO_RIGHT){
            drawText(text, canvas, mOriginPaint, middle, getWidth());
        }else{
            drawText(text, canvas, mOriginPaint, 0, getWidth() - middle);
        }
    }

    private void drawText(String text, Canvas canvas, Paint paint, int start, int end) {
        // 保持画布状态
        canvas.save();
        // 只绘制截取部分
        canvas.clipRect(new Rect(start, 0, end, getHeight()));
        // 获取字体的bounds
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        // x  就是代表绘制的开始部分  不考虑左右padding不相等的情况下 = 控件宽度的一半 - 字体宽度的一半
        int x = (getWidth() - bounds.width()) / 2;
        // y  代表的是基线 baseLine
        int dy = bounds.height() / 2 - bounds.bottom;
        // 计算基线的位置
        int baseLine = (getHeight() + bounds.height()) / 2 - dy;
        canvas.drawText(text, x, baseLine, paint);
        // 释放画布
        canvas.restore();
    }

    /**
     * 1. 设置当前的进度
     * @param currentProgress  当前进度
     */
    public void setCurrentProgress(float currentProgress){
        this.mCurrentProgress = currentProgress;
        // 重新绘制  会不断的调用onDraw方法
        invalidate();
    }


    /**
     * 2.设置不同的朝向
     * @param direction  当前朝向
     */
    public void setDirection(Direction direction){
        mDirection = direction;
    }

    /**
     * 3.设置原始不变色的字体颜色
     */
    public void setOriginColor(int color){
        mOriginPaint.setColor(color);
    }

    /**
     * 3.设置变色的字体颜色
     */
    public void setChangeColor(int color){
        mChangePaint.setColor(color);
    }
}
