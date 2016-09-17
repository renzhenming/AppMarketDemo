package com.example.appmarket.thread;

import java.io.File;

import android.os.Environment;

import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.utils.UIUtils;

public class DownloadEntity {
	
	private int id;
	private String name;
	private long size;
	private String downloadUrl;
	
	private int currentState;
	private long currentPosi;
	private String path;//下载文件存储路径
	
	//获取当前下载进度
	public float getCurrentProgress(){
		if (size == 0) {
			return 0;
		}
		float currentProgress = (float)currentPosi / size;
		return currentProgress;
	}
	
	//copyappinfo到downloadinfo
	public static DownloadEntity copy(AppEntity appEntity){
		DownloadEntity downloadEntity = new DownloadEntity();
		downloadEntity.id = appEntity.getId();
		downloadEntity.name = appEntity.getName();
		downloadEntity.size = appEntity.getSize();
		downloadEntity.downloadUrl = appEntity.getDownloadUrl();
		downloadEntity.currentPosi = 0;
		downloadEntity.currentState = DownloadManager.STATE_NONE;
		downloadEntity.path = downloadEntity.getPath();
				
		return downloadEntity;
	}
	
	//获取下载文件保存路径
	public String getPath(){
		//文件夹路径
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/appmarket/download";
		if (createDir(path)) {
			return path + "/"+name+".apk";
		}
		return null;
	}
	public boolean createDir(String path){
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
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

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public long getCurrentPosi() {
		return currentPosi;
	}

	public void setCurrentPosi(long currentPosi) {
		this.currentPosi = currentPosi;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
















