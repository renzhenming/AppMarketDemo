package com.example.appmarket.ui.view;

import com.example.appmarket.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = typedArray.getFloat(R.styleable.RatioLayout_ratio, 1);
		typedArray.recycle();
	}

	public RatioLayout(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		//用获取到的宽度/宽高比例就可以的到需要的高度（减去两边可能设置的padding 值
		int widthOfImage = widthSize - getPaddingLeft() - getPaddingRight();
		//image的高度
		int heightOfImage = (int) (widthOfImage / ratio + 0.5f);
		//ratiolayout的高度
		int height = heightOfImage + getPaddingBottom() + getPaddingTop();
		//根据得到的高度和模式重新得到heighMeasureSpec
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	
}
