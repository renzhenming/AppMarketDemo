package com.example.appmarket.http.protocol;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RecommendProtocol extends BaseProtocol<ArrayList<String>> {

	@Override
	public ArrayList<String> parseJsonToEntity(String data) {
		Gson gson = new Gson();
		ArrayList<String> list = gson.fromJson(data, new TypeToken<List<String>>(){}.getType());
		return list;
	}

	@Override
	public String getKey() {
		return "recommend";
	}

	@Override
	public String getParams() {
		return "";
	}

}
