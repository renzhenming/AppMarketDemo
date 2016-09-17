package com.example.appmarket.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.http.HttpHelper.HttpResult;
import com.example.appmarket.http.StringUtils;
import com.example.appmarket.utils.IOUtils;
import com.example.appmarket.utils.UIUtils;

public abstract class BaseProtocol<T> {
	//读取缓存
	public String getCache(int index){
		File cacheDir = UIUtils.getContext().getCacheDir();
		File cacheFile = new File(cacheDir, getKey()+"?index="+index+getParams());
		if (cacheFile != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				
				//读取缓存的有效期
				String cacheUsableTime = reader.readLine();//如果不将这一行读取出来的话，那么拿到的string字符串就无法作为json被解析
				if (System.currentTimeMillis() < Long.parseLong(cacheUsableTime)) {
					//说明缓存还有效
					String str = null;
					StringBuffer sb = new StringBuffer();
					while((str = reader.readLine())!= null){
						sb.append(str);
					}
					return sb.toString();
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				IOUtils.close(reader);
			}
		}
		return null;
	}
	
	//写入缓存
	public void setCache(String string,int index){
		
		File cacheDir = UIUtils.getContext().getCacheDir();
		File cacheFile = new File(cacheDir, getKey()+"?index="+index+getParams());
		
		FileWriter writer = null;
		try {
			writer = new FileWriter(cacheFile);
			//设置缓存有效期为30分钟，换行是为了读取的时候读取到有效期，可以用readline方法读取到第一行就是有效期
			writer.write(System.currentTimeMillis()+30*60*1000+"\n");
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(writer);
		}
	}
	
	//将字符串解析成对象返回
	public T getEntity(int index){
		//先去缓存中读取数据，如果为空在请求服务器
		String result = getCache(index);
		System.out.println("缓存为："+result);
		if (StringUtils.isEmpty(result)) {
			result = getDataFromServer(index);
			System.out.println("从网络拿到的数据为："+result);
		}
		if (!StringUtils.isEmpty(result)) {
			return parseJsonToEntity(result);
		}
		
		return null;
	}
	
	public abstract T parseJsonToEntity(String data);

	//从服务器拿到字符串数据
	public String getDataFromServer(int index){
		HttpResult result = HttpHelper.get(HttpHelper.URL+getKey()+"?index="+index+getParams());
		if (result != null) {
			String string = result.getString();
			//获取到数据后写一份到缓存中
			if (!StringUtils.isEmpty(string)) {
				setCache(string, index);
			}
			
			return string;
		}
		return null;
	}
	
	public abstract String getKey();
	public abstract String getParams();
}
