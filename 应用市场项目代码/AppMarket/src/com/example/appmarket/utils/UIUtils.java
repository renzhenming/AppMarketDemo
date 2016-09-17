package com.example.appmarket.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.example.appmarket.global.AppMarcketApplication;

public class UIUtils {
	
	//��ȡ���ȫ�ֱ���
	public static Context getContext(){
		return AppMarcketApplication.context;
	}
	
	public static Handler getHandler(){
		return AppMarcketApplication.handler;
	}
	
	public static int getMainThreadId(){
		return AppMarcketApplication.mainThreadId;
	}
	
	////////////////////////////////////////////////////
	
	/**��ȡ��Դ�ļ�*/
	
	public static String getString(int id){
		return getContext().getResources().getString(id);
	}
	
	public static int getColor(int id){
		return getContext().getResources().getColor(id);
	}
	
	public static int getDimen(int id){
		return getContext().getResources().getDimensionPixelSize(id);
	}
	
	public static String[] getStringArray(int id){
		return getContext().getResources().getStringArray(id);
	}
	
	public static Drawable getDrawable(int id){
		return getContext().getResources().getDrawable(id);
	}
	
	/////////////////////////////////////////////////////////////////
	public static int dp2px(int dp){
		return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
	}
	
	public static int px2dp(float px){
		return (int) (px / getContext().getResources().getDisplayMetrics().density);
	}
	
	/////////////////////////////////////////////////////////////////
	public static View inflate(int id){
		return View.inflate(getContext(), id, null);
	}
	
	public static boolean isRunOnUiThread(){
		return getMainThreadId() == Process.myTid();
	}
	
	public static void runOnUiThread(Runnable r){
		if (isRunOnUiThread()) {
			r.run();
		}else{
			getHandler().post(r);
		}
	}

	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		
		return getContext().getResources().getColorStateList(mTabTextColorResId);
	}
}























