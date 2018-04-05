package com.example.appmarket.entity;

import java.util.ArrayList;
import java.util.List;

public class AppInfoMain<AppEntity> {
	private ArrayList<AppEntity> d;

	public ArrayList<AppEntity> getAppEntityList() {
		return d;
	}

	public void setAppEntityList(ArrayList<AppEntity> d) {
		this.d = d;
	}
}
