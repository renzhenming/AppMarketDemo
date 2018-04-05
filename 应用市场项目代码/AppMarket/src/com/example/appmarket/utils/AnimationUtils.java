package com.example.appmarket.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class AnimationUtils {

	public static GradientDrawable getGradientDrawable(int color,int radius){
		GradientDrawable dra = new GradientDrawable();
		dra.setShape(GradientDrawable.RECTANGLE);
		dra.setColor(color);
		dra.setCornerRadius(radius);
		return dra;
	}
	
	public static StateListDrawable getStateListDrawable(Drawable presseddrawable,Drawable normaldrawable){
		StateListDrawable draw = new StateListDrawable();
		draw.addState(new int[]{android.R.attr.state_pressed}, presseddrawable);
		draw.addState(new int[]{}, normaldrawable);
		return draw;
	}
}
