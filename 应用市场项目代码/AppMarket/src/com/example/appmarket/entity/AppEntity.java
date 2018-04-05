package com.example.appmarket.entity;

import java.util.ArrayList;

/***
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
public class AppEntity {
	private int id;
	private String name;
	private String packageName;
	private String iconUrl;
	private float stars;
	private long size;
	private String downloadUrl;
	private String des;
	
	//扩展字段，用于应用详情页
	private String author;
	private String date;
	private String downloadNum;
	private ArrayList<SafeBean> safe;
	private ArrayList<String> screen;
	private String version;
	
	public class SafeBean {
		private String safeDes;
		private String safeDesColor;
		private String safeDesUrl;
		private String safeUrl;
		public String getSafeDes() {
			return safeDes;
		}
		public void setSafeDes(String safeDes) {
			this.safeDes = safeDes;
		}
		public String getSafeDesColor() {
			return safeDesColor;
		}
		public void setSafeDesColor(String safeDesColor) {
			this.safeDesColor = safeDesColor;
		}
		public String getSafeDesUrl() {
			return safeDesUrl;
		}
		public void setSafeDesUrl(String safeDesUrl) {
			this.safeDesUrl = safeDesUrl;
		}
		public String getSafeUrl() {
			return safeUrl;
		}
		public void setSafeUrl(String safeUrl) {
			this.safeUrl = safeUrl;
		}
		
		
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	
	public ArrayList<SafeBean> getSafe() {
		return safe;
	}
	public void setSafe(ArrayList<SafeBean> safe) {
		this.safe = safe;
	}
	public ArrayList<String> getScreen() {
		return screen;
	}
	public void setScreen(ArrayList<String> screen) {
		this.screen = screen;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public float getStars() {
		return stars;
	}
	public void setStars(float stars) {
		this.stars = stars;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
}
