package com.example.appmarket.http.protocol;

import com.example.appmarket.entity.AppEntity;
import com.google.gson.Gson;

public class AppDetailProtocol extends BaseProtocol<AppEntity> {
	
	private String packageName;

	public AppDetailProtocol(String packageName){
		this.packageName = packageName;
	}
	@Override
	public AppEntity parseJsonToEntity(String data) {
		Gson gson = new Gson();
		AppEntity entity = gson.fromJson(data, AppEntity.class);
		return entity;
	}

	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public String getParams() {
		return "&packageName="+packageName;
	}

}
