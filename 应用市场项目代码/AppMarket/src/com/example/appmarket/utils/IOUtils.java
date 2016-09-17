package com.example.appmarket.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/** 鍏抽棴娴�*/
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
