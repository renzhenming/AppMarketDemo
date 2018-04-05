package com.example.appmarket.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.appmarket.entity.AppEntity;
/**
 *  "id": 1525490,
            "name": "有缘网",
            "packageName": "com.youyuan.yyhl",
            "iconUrl": "app/com.youyuan.yyhl/icon.jpg",
            "stars": 4,
            "size": 3876203,
            "downloadUrl": "app/com.youyuan.yyhl/com.youyuan.yyhl.apk",
            "des": "产品介绍：有缘是时下最受大众单身男女亲睐的婚恋交友软件。有缘网专注于通过轻松、"
 * @author Administrator
 *
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppEntity>> {
	ArrayList<String> viewpagerList = new ArrayList<String>();
	@Override
	public ArrayList<AppEntity> parseJsonToEntity(String data) {
		
		System.out.println("data:"+data);
		try {
			
			ArrayList<AppEntity> list = new ArrayList<AppEntity>();
			JSONObject jo = new JSONObject(data);
			//轮播条对象
			JSONArray vieapgerJa = jo.getJSONArray("picture");
			for (int i = 0; i < vieapgerJa.length(); i++) {
				String string = vieapgerJa.getString(i);
				viewpagerList.add(string);
			}
			
			//应用对象
			JSONArray ja = jo.getJSONArray("list");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject j2o = ja.getJSONObject(i);
				AppEntity entity = new AppEntity();
				entity.setDes(j2o.getString("des"));
				entity.setDownloadUrl(j2o.getString("downloadUrl"));;
				entity.setIconUrl(j2o.getString("iconUrl"));
				entity.setId(j2o.getInt("id"));
				entity.setName(j2o.getString("name"));
				entity.setPackageName(j2o.getString("packageName"));
				entity.setSize(j2o.getLong("size"));
				entity.setStars((float) j2o.getDouble("stars"));
				
				list.add(entity);
			}
			
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return "";
	}
	
	//本方法用于获取轮播条的数据集合
	public ArrayList<String> getPictures(){
		return viewpagerList;
	}

}
