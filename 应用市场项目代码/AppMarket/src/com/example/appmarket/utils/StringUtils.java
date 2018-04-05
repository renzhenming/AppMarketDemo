package com.example.appmarket.utils;


public class StringUtils {
	/** 鍒ゆ柇瀛楃涓叉槸鍚︽湁鍊硷紝濡傛灉涓簄ull鎴栬�鏄┖瀛楃涓叉垨鑰呭彧鏈夌┖鏍兼垨鑰呬负"null"瀛楃涓诧紝鍒欒繑鍥瀟rue锛屽惁鍒欏垯杩斿洖false */
	public static boolean isEmpty(String value) {
		//TextUtils.isEmpty(str)
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
}
