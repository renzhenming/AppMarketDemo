package com.example.appmarket.http.protocol;

import java.util.ArrayList;
import java.util.List;

import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.entity.AppInfoMain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppProtocol extends BaseProtocol<ArrayList<AppEntity>> {
	/**
	 *  "id": 1580615,
        "name": "人人",
        "packageName": "com.renren.mobile.android",
        "iconUrl": "app/com.renren.mobile.android/icon.jpg",
        "stars": 2,
        "size": 21803987,
        "downloadUrl": "app/com.renren.mobile.android/com.renren.mobile.android.apk",
        "des": "2005-2014 你的校园一直在这儿。中国最大的实名制SNS网络平台，大学生"
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AppEntity> parseJsonToEntity(String data) {
		Gson gson = new Gson();
		ArrayList<AppEntity> list = gson.fromJson(data, new TypeToken<List<AppEntity>>(){}.getType());
		
		if (list != null) {
			
			return list;
		}
		return null;
	}

	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public String getParams() {
		return "";
	}

}
