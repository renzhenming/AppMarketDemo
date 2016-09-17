package com.example.appmarket.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class AppMarcketApplication extends Application {
	
	public static Context context;
	public static Handler handler;
	public static int mainThreadId;

	@Override
	public void onCreate() {
		
		super.onCreate();
		initGlobalVariables();
	}

	private void initGlobalVariables() {
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();
	}
	//生成getter方法获取变量
	public Context getContext() {
		return context;
	}

	public Handler getHandler() {
		return handler;
	}

	public int getMainThreadId() {
		return mainThreadId;
	}
	
	
	
}





















