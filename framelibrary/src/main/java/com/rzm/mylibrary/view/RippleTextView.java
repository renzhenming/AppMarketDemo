package com.rzm.mylibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by renzhenming on 2018/3/22.
 * 颜色渐变效果的TextView，可用于音乐播放的字幕
 */

public class RippleTextView extends TextView{

    //最初的画笔
    private Paint mOriginPaint;

    //颜色改变后的画笔
    private Paint mChangedPaint;

    //当前滑动进度
    private float mCurrentPercent = 0.6f;

    //当前设置的文本
    private String mText;

    // 当前朝向
    private Direction mDirection = Direction.DIRECTION_LEFT;

    // 绘制的朝向枚举
    public enum Direction {
        DIRECTION_LEFT, DIRECTION_RIGHT
    }


    public RippleTextView(Context context) {
        this(context,null);
    }

    public RippleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public RippleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mOriginPaint = getPaintByColor(Color.BLACK);
        mChangedPaint = getPaintByColor(Color.RED);
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setColor(color);
        // 字体的大小设置为TextView的文字大小
        paint.setTextSize(getTextSize());
        return paint;
    }

    /**
     * 自己绘制，不继承super
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //获取textView的文本
        mText = getText().toString().trim();
        //获取textView宽度
        int width = getWidth();
        if (!TextUtils.isEmpty(mText)){
            //mCurrentPercent进度下，滑动到控件的位置
            int position = (int) (width* mCurrentPercent);

            // 根据不同的朝向去画字体
            if (mDirection == Direction.DIRECTION_LEFT) {
                drawOriginDirectionLeft(canvas, position);
                drawChangeDirectionLeft(canvas, position);
            }
            if (mDirection == Direction.DIRECTION_RIGHT) {
                drawOriginDirectionRight(canvas, position);
                drawChangeDirectionRight(canvas, position);
            }

        }
    }

    /**
     * 画朝向右边变色字体
     */
    private void drawChangeDirectionRight(Canvas canvas, int position) {
        drawText(canvas, mChangedPaint, getWidth() - position, getWidth());
    }
    /**
     * 画朝向左边默认色字体
     */
    private void drawOriginDirectionRight(Canvas canvas, int position) {
        drawText(canvas, mOriginPaint, 0, getWidth() - position);
    }
    /**
     * 画朝向左边变色字体
     */
    private void drawChangeDirectionLeft(Canvas canvas, int position) {
        drawText(canvas, mChangedPaint, 0, position);
    }
    /**
     * 画朝向左边默认色字体
     */
    private void drawOriginDirectionLeft(Canvas canvas, int position) {
        drawText(canvas, mOriginPaint, position, getWidth());
    }
    /**
     * 设置当前的进度
     * @param currentProgress 当前进度
     */
    public void setCurrentProgress(float currentProgress) {
        this.mCurrentPercent = currentProgress;
        // 重新绘制
        invalidate();
    }
    /**
     * 设置绘制方向，从右到左或者从左到右
     * @param direction 绘制方向
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    /**
     * 根据滑动位置绘制文本
     * @param canvas
     * @param paint
     * @param start
     * @param end
     *
     * 基准点是baseline
     * Ascent是baseline之上至字符最高处的距离
     * Descent是baseline之下至字符最低处的距离
     * Top指的是指的是最高字符到baseline的值，即ascent的最大值，
     * bottom指的是最下字符到baseline的值，即descent的最大值，字能达到的最小值
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        //保存画笔状态
        canvas.save();

        //截取绘制的内容，该方法用于裁剪画布，也就是设置画布的显示区域
        //主要用于部分显示以及对画布中的部分对象进行操作的场合。
        canvas.clipRect(start,0,end,getHeight());

        //获取文字的范围
        Rect bounds = new Rect();
        mOriginPaint.getTextBounds(mText,0,mText.length(),bounds);

        // 获取文字的Metrics 用来计算基线
        Paint.FontMetricsInt fontMetrics = mOriginPaint.getFontMetricsInt();

        //获取文字的高度
        int fontHeight = fontMetrics.bottom - fontMetrics.top;

        // 计算基线到中心点的位置，fontHeight/2是文字中心点， fontMetrics.bottom是基线到文字最低点的高度，
        int offY = fontHeight/2 - fontMetrics.bottom;

        // 计算基线位置,为什么基线的位置这样计算，如果想不通，可以这样，假设，文字在控件正中间，控件高度的一半
        //也就是控件顶部到文字中心点的垂直距离，文字高度的一半也就是文字中心点到文字最顶部或者最底部的距离，二者相加
        //可以看作是从控件顶部到文字最底部的距离，这个距离是等于控件顶部到基线的距离加上
        int baseline = (getMeasuredHeight() + fontHeight) / 2 - offY;

        //绘制文字
        canvas.drawText(mText,getMeasuredWidth()/2-bounds.width()/2,baseline,paint);

        //恢复画笔状态
        canvas.restore();
    }

}



















