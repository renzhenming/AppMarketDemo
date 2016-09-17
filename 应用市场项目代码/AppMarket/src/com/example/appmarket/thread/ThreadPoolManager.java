package com.example.appmarket.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

	private static ThreadPool pool;
	private ThreadPoolManager(){}
	
	public static ThreadPool getThreadPool(){
		
		if (pool == null) {
			synchronized (ThreadPoolManager.class) {
				if (pool == null) {
					
					long aliveTime = 1;
					int size = Runtime.getRuntime().availableProcessors()*2+1;
					int maximumPoolSize = size + 3;
					pool = new ThreadPool(size, maximumPoolSize, aliveTime);
				}
			}
			
		}		
		return pool;
	}
	public static class ThreadPool {
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;
		private TimeUnit unit = TimeUnit.SECONDS;
		private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();
		private ThreadFactory threadFactory = Executors.defaultThreadFactory();
		private AbortPolicy handler = new AbortPolicy();
		private ThreadPoolExecutor executor;

		private ThreadPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
			this.corePoolSize = corePoolSize;
			this.keepAliveTime = keepAliveTime;
			this.maximumPoolSize = maximumPoolSize;
		}
		
		public void execute(Runnable r){
			if (executor == null) {
				executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
				
			}
			executor.execute(r);
		
		}
		
		//移除下载任务(只对位于下载队列中的任务有效)
		public void remove(Runnable r){
			if (executor !=null) {
				executor.getQueue().remove(r);
			}
			
		}
	}
}




















