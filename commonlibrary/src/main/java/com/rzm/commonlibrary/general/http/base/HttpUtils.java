package com.rzm.commonlibrary.general.http.base;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rzm on 2017/8/20.
 * 将各种网络引擎进行包装，后期如果需要跟换网络引擎，直接修改简单的代码就可以实现了
 *
 * 依赖倒置原则：指代了一种特定的解耦形式，高层模块不依赖低层次模块的细节，通俗地说高层次就是不依赖细节而是依赖抽象。
 * 那什么又是低层次什么是高层次？
 * 针对我们这个网络框架来说 HttpUtils 是高层次，IHttpEngine、OkHttpEngine等一些具体的网络引擎是低层次，HttpUtils
 * 不依赖于网络请求的具体细节，细节交给OkHttpEngine去实现，这同时符合单一职责原则
 */
public class HttpUtils{

    private String mUrl;
    private String mPath;

    //默认请求类型为get方式
    private int mType = GET_TYPE;

    private static final int POST_TYPE = 0x0011;

    private static final int GET_TYPE = 0x0022;

    private static final int DOWNLOAD_TYPE = 0x0033;

    private static final int UPLOAD_TYPE = 0x0044;

    private Context mContext;

    /**
     * 面向对象原则之：开闭原则，接口实现网络引擎的切换
     * 开闭原则：
     * 软件中的对象（类、模块、函数等）应该对于扩展是开放的，但是，对于修改是封闭的
     */
    private static IHttpEngine mHttpEngine;

    private Map<String,Object> mParams;

    //默认未设置缓存
    private boolean mCache = false;

    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }

    private HttpUtils(){

    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    public HttpUtils download(){
        mType = DOWNLOAD_TYPE;
        return this;
    }

    public HttpUtils upload(){
        mType = UPLOAD_TYPE;
        return this;
    }

    /**
     * 添加参数
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParams(String key,Object value){
        mParams.put(key,value);
        return this;
    }

    public HttpUtils addParams(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    /**
     * 网络请求的url
     * @param url
     * @return
     */
    public HttpUtils url(String url){
        mUrl = url;
        return this;
    }

    /**
     * 上传文件的本地地址
     * @param path
     * @return
     */
    public HttpUtils path(String path){
        mPath = path;
        return this;
    }

    /**
     * 设置是否添加缓存,如果设置为true，要使用缓存功能，一定要设置缓存引擎
     */
    public HttpUtils cache(boolean cache){
        mCache = cache;
        return this;
    }
    /**
     * 执行 设置回掉
     * @return
     */
    public HttpUtils execute(ICallBack callBack){
        if (callBack == null){
            callBack = ICallBack.DEFAULT_CALL_BACK;
        }

        callBack.onPreExecute(mContext,mParams);

        //判断执行方法
        if (mType == POST_TYPE){
            post(mContext,mUrl,mParams,callBack);
        }
        if (mType == GET_TYPE){
            get(mContext,mUrl,mParams,callBack);
        }
        if (mType == DOWNLOAD_TYPE){
            download(mUrl,callBack);
        }
        if (mType == UPLOAD_TYPE){
            upload(mPath,mUrl,callBack);
        }
        return this;
    }

    /**
     * 初始化网络引擎
     * 在application中初始化一次
     * @param httpEngine
     */
    public static void initHttpEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    /**
     * 初始化缓存引擎
     * 在application中初始化一次
     * @param httpCache
     */
    public static void initCacheEngine(IHttpCache httpCache){
        //设置缓存引擎
        HttpCacheUtils.getInstance().init(httpCache);
    }

    /**
     *
     * 里氏替换原则：简单来说就是，所有引用基类的地方必须能透明地使用其子类的对象。
     * 通俗点讲，只要父类能出现的地方子类就可以出现，但是，有子类出现的地方，父类未必就能适应
     *
     * HttpUtils.initHttpRequest(new XUtilsRequest());
     * HttpUtils.initHttpRequest(new OKHttpRequest());
     * RecyclerView 的 LayoutMananger
     * mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
     * mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
     *
     * 切换引擎
     * @param httpEngine
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }

    /**
     * 切换缓存引擎
     * @param httpCache
     */
    public HttpUtils exchangeCacheEngine(IHttpCache httpCache){
        //切换缓存引擎
        HttpCacheUtils.getInstance().exchangeCacheEngine(httpCache);
        return this;
    }


    /**
     * 执行请求
     * @param url
     * @param params
     * @param callBack
     */
    private void get(Context context,String url, Map<String, Object> params, ICallBack callBack) {
        mHttpEngine.get(mCache,context,url,params,callBack);
    }

    private void post(Context context,String url, Map<String, Object> params, ICallBack callBack) {
        mHttpEngine.post(mCache,context,url,params,callBack);
    }

    private void download(String url,ICallBack callBack) {
        mHttpEngine.download(url,callBack);
    }

    private void upload(String path, String url, ICallBack callBack) {
        mHttpEngine.upload(path,url,callBack);
    }

    //--------------------------------  供外界调用 --------------------------------//

    /**
     * 拼接参数（为了打印显示提供）
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的class信息
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
