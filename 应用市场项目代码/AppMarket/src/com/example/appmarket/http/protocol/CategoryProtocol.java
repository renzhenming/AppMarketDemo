package com.example.appmarket.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.appmarket.entity.CategoryEntity;

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryEntity>> {

	@Override
	public ArrayList<CategoryEntity> parseJsonToEntity(String data) {
		try {
			ArrayList<CategoryEntity> list = new ArrayList<CategoryEntity>();
			JSONArray ja = new JSONArray(data);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);

					CategoryEntity titleEntity = new CategoryEntity();
					String title = jo.getString("title");
					titleEntity.setTitle(title);
					titleEntity.setIsTitle(true);
					
					list.add(titleEntity);

					JSONArray ja2 = jo.getJSONArray("infos");
					for (int j = 0; j < ja2.length(); j++) {
						JSONObject jo2 = ja2.getJSONObject(j);
						CategoryEntity normalEntity = new CategoryEntity();
						normalEntity.setName1(jo2.getString("name1"));
						normalEntity.setName2(jo2.getString("name2"));
						normalEntity.setName3(jo2.getString("name3"));
						normalEntity.setUrl1(jo2.getString("url1"));
						normalEntity.setUrl2(jo2.getString("url2"));
						normalEntity.setUrl3(jo2.getString("url3"));
						normalEntity.setIsTitle(false);
						list.add(normalEntity);
					}
				
				
			
				
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParams() {
		return "";
	}

}
