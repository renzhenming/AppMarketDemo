package com.example.appmarket.entity;

public class CategoryEntity {

	private String title;
	private boolean isTitle;
	private String name1;
	private String name2;
	private String name3;
	private String url1;
	private String url2;
	private String url3;
	
	public boolean isTitle() {
		return isTitle;
	}
	public void setIsTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	public String getUrl3() {
		return url3;
	}
	public void setUrl3(String url3) {
		this.url3 = url3;
	}
	@Override
	public String toString() {
		return "CategoryEntity [title=" + title + ", isTitle=" + isTitle
				+ ", name1=" + name1 + ", name2=" + name2 + ", name3=" + name3
				+ ", url1=" + url1 + ", url2=" + url2 + ", url3=" + url3 + "]";
	}
	
	
	
}
