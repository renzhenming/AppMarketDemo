package com.example.appmarket.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.http.HttpHelper.HttpResult;
import com.example.appmarket.utils.UIUtils;

public class DownloadManager {
	
	//观察者集合
	private ArrayList<DownloadObserver> list = new ArrayList<DownloadObserver>();
	//下载对象集合
	private ConcurrentHashMap<Integer, DownloadEntity> downloadEntityList = new ConcurrentHashMap<Integer, DownloadEntity>();
	//下载任务集合
	private ConcurrentHashMap<Integer, DownloadTask> downloadTaskList = new ConcurrentHashMap<Integer, DownloadTask>();
	/**
	 * 下载状态
	 */
	public static final int STATE_NONE = 0;//未下载
	public static final int STATE_WAITING = 1;//等待下载
	public static final int STATE_DOWNLOAD = 2;//正在下载
	public static final int STATE_PAUSE = 3;//暂停下载
	public static final int STATE_ERROR = 4;//下载失败
	public static final int STATE_SUCCESS = 5;//下载成功
	/***
	 * 单例模式
	 */
	private static DownloadManager manager = new DownloadManager();
	private DownloadManager(){}
	
	public static DownloadManager getDownloadManager(){
		return manager;
	}
	
	/**
	 * 用于下载
	 */
	public synchronized void download(AppEntity appEntity){
		if (appEntity != null) {
			//当用户点击暂停后再次开始下载不能重新new对象，而应该拿到原来的对象，在断开的位置继续下载，那么就需要首先重集合中拿对象
			//如果集合中有此对象，说明之前下载过，需要接着下载，如果没有说明这是首次下载，需要重新开始
			DownloadEntity downloadEntity = downloadEntityList.get(appEntity.getId());
			System.out.println("jihezhongyouma:"+downloadEntity);
			
			if (downloadEntity == null) {
				downloadEntity = DownloadEntity.copy(appEntity);
				System.out.println("重新创建下载对象");
			}
			
			//进入该方法表示进入下载状态，这时候要跟新当前下载的状态
			downloadEntity.setCurrentState(STATE_WAITING);
			//下载状态改变，通知观察者
			notifyDownloadStateChanged(downloadEntity);
			
			//下载时可能会产生很多个下载对象，放入集合中维护
			downloadEntityList.put(downloadEntity.getId(), downloadEntity);
			// 开始下载,将下载对象通过构造方法传入
			DownloadTask task = new DownloadTask(downloadEntity);
			ThreadPoolManager.getThreadPool().execute(task);
			//将下载任务保存在集合中
			downloadTaskList.put(downloadEntity.getId(), task);
		}
		
	}
	
	class DownloadTask implements Runnable {

		private DownloadEntity downloadEntity;
		
		public DownloadTask(DownloadEntity downloadEntity) {
			this.downloadEntity = downloadEntity;
		}

		@Override
		public void run() {
			//更新下载状态
			downloadEntity.setCurrentState(STATE_DOWNLOAD);
			notifyDownloadStateChanged(downloadEntity);
			// 执行下载的一系列动作
			File file = new File(downloadEntity.getPath());//文件下载后保存的位置
			HttpResult httpResult;
			if (!file.exists() || file.length() != downloadEntity.getCurrentPosi() || downloadEntity.getCurrentPosi() == 0) {
				
				//1.说明这是第一次下载，不需要断点续传，从0开始下载2.说明文件下载中丢掉了字节导致文件不完整3.下载位置为0
				file.delete();
				//删除后将当前下载位置设置为0
				downloadEntity.setCurrentPosi(0);
				//开始下载
				httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadEntity.getDownloadUrl());
				
			}else{

				//从上次断开的位置开始下载
				httpResult = HttpHelper.download(HttpHelper.URL+"download?name="+downloadEntity.getDownloadUrl()+"&range="+file.length());
			}
			
			InputStream inputStream = null;
			FileOutputStream fos = null;
			if (httpResult != null && httpResult.getInputStream() != null) {
				inputStream = httpResult.getInputStream();
				try {
					fos = new FileOutputStream(file,true);
					byte[] bytes = new byte[1024];
					int len = 0;
					//之前设置的pause方法并不能完全的暂停下载任务，这里添加一个状态的判断downloadEntity.getCurrentState() == STATE_DOWNLOAD
					//每次下载都判断一次，当调用pause方法暂停后将当前状态改变为state_pause所以下载到这里一判断发现状态条件不满足就停止下载了
					while((len = inputStream.read(bytes))!= -1 && downloadEntity.getCurrentState() == STATE_DOWNLOAD){
						fos.write(bytes, 0, len);
						fos.flush();
						//每写入一次，就更新一次当前下载位置，并通知观察者
						downloadEntity.setCurrentPosi(downloadEntity.getCurrentPosi()+len);
						notifyDownloadProgressChanged(downloadEntity);
						boolean exists = file.exists();
					}
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//下载完成校验文件的正确性
				if (file.length() == downloadEntity.getSize()) {
					//完整
					downloadEntity.setCurrentState(STATE_SUCCESS);
					notifyDownloadProgressChanged(downloadEntity);
				}else if(downloadEntity.getCurrentState() == STATE_PAUSE){
					notifyDownloadStateChanged(downloadEntity);
				}else{
					downloadEntity.setCurrentState(STATE_ERROR);
					file.delete();
					downloadEntity.setCurrentPosi(0);
					notifyDownloadStateChanged(downloadEntity);
				}
			}else{
				//下载失败
				downloadEntity.setCurrentState(STATE_ERROR);
				file.delete();
				downloadEntity.setCurrentPosi(0);
				notifyDownloadStateChanged(downloadEntity);
			}
			
			//下载结束，从duixiang.任务集合中移除下载任务
			downloadTaskList.remove(downloadEntity.getId());
			//本代码如果存在，将导致无法进行断电续传，因为每次下载都会将下载对象从集合中删除，导致每次都需要重新创建对象，
			//并且，会导致下载成功之后退出页面再次进入，无法安装，因为对象已经被清除，初始化页面显示“下载”，因此，从下载对象集合中
			//移除下载对象的动作必须放在安装完成之后方可进行
//			downloadEntityList.remove(downloadEntity.getId());
		}
		
	}
	/**
	 * 安装
	 * @param downloadEntity
	 */
	public synchronized void install(AppEntity appEntity){
		if (appEntity !=null) {
			DownloadEntity downloadEntity = downloadEntityList.get(appEntity.getId());
			if (downloadEntity != null) {
				 // 跳转到系统安装页面  
                Intent intent = new Intent(Intent.ACTION_VIEW);  
                intent.addCategory(Intent.CATEGORY_DEFAULT);  
                intent.setDataAndType(Uri.parse("file://"+downloadEntity.getPath()),  
                        "application/vnd.android.package-archive");  
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UIUtils.getContext().startActivity(intent); 
                
			}
		}
		
	}
	/**
	 * 暂停下载
	 * 只有在正在下载和等待下载状态下才可以暂停下载
	 * @param downloadEntity
	 */
	public synchronized void pause(AppEntity appEntity){
		if (appEntity != null) {
			//获取到需要暂停的下载对象，此对象一定在下载对象集合中
			DownloadEntity downloadEntity = downloadEntityList.get(appEntity.getId());
			if (downloadEntity != null) {
				//只有在满足当前下载状态为正在下载或者等待下载条件下才能暂停
				if (downloadEntity.getCurrentState() == STATE_DOWNLOAD || downloadEntity.getCurrentState() == STATE_WAITING){ 
					
					//从下载队列中移除此对象的下载任务(只对位于下载队列中的对象有作用，对正在下载的对象无效),现获取到此任务
					DownloadTask downloadTask = downloadTaskList.get(appEntity.getId());
					ThreadPoolManager.getThreadPool().remove(downloadTask);
					//更改下载状态，通知观察者
					downloadEntity.setCurrentState(STATE_PAUSE);
					notifyDownloadStateChanged(downloadEntity);
				}
				
			}
		}
		
	}
	//通知
	public void notifyDownloadStateChanged(DownloadEntity downloadEntity){
		for (DownloadObserver downloadObserver : list) {
			downloadObserver.onDownloadStateChanged(downloadEntity);
		}
	}
	
	public void notifyDownloadProgressChanged(DownloadEntity downloadEntity){
		for (DownloadObserver downloadObserver : list) {
			downloadObserver.onDownloadProgressChanged(downloadEntity);
		}
	}
	
	//声明观察者接口
	public interface DownloadObserver {
		public void onDownloadStateChanged(DownloadEntity downloadEntity);
		public void onDownloadProgressChanged(DownloadEntity downloadEntity);
	}
	//注册观察者
	public void registerDownloadObserver(DownloadObserver observer){
		if (observer != null && !list.contains(observer)) {
			list.add(observer);
		}
		
	}
	//注销观察者
	public void unRegisterDownloadObserver(DownloadObserver observer){
		if (observer != null && list.contains(observer)) {
			list.remove(observer);
		}
	}
	
	//本方法用于提供给调用者获取下载对象
	public DownloadEntity getDownloadEntity(int key){
		if (downloadEntityList != null) {
			DownloadEntity downloadEntity = downloadEntityList.get(key);
			return downloadEntity;
		}
		return null;
	}
}
















