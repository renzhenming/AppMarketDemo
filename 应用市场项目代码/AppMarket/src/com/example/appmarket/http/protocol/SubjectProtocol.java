package com.example.appmarket.http.protocol;

import java.util.ArrayList;
import java.util.List;

import com.example.appmarket.entity.SubjectEntity;
import com.example.appmarket.utils.GsonTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectEntity>> {

	@Override
	public ArrayList<SubjectEntity> parseJsonToEntity(String data) {
		Gson gson = new Gson();
		ArrayList<SubjectEntity> list = gson.fromJson(data, new TypeToken<List<SubjectEntity>>(){}.getType());
		System.out.println("list:"+list);
		return list;
	}

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return "";
	}

}
