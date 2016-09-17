package com.example.appmarket.utils;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
	
	private static BitmapUtils mBitmapUtils = null;
	
	private BitmapHelper(){}
	
	public static BitmapUtils getBitmapUtils(){
		if (mBitmapUtils == null) {
			synchronized (BitmapHelper.class) {
				if (mBitmapUtils == null) {
					mBitmapUtils = new BitmapUtils(UIUtils.getContext());
				}
				
			}
			
		}
		return mBitmapUtils;
	}
}
