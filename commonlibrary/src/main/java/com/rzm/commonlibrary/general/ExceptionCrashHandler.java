package com.rzm.commonlibrary.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import com.rzm.commonlibrary.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by rzm on 2017/8/10.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "ExceptionCrashHandler";
    private static final String CRASH_FILE_NAME = "crash_file_name";
    private static ExceptionCrashHandler mInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;


    private ExceptionCrashHandler(){}

    public static ExceptionCrashHandler getInstance(){
        if (mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if (mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    /**
     * 需要上传的信息
     *
     * 1.崩溃的详细信息
     * 2.应用信息 包名 版本号
     * 3.手机信息
     * 4.上传问题，上传文件不在这里处理，保存文件等应用再次启动时上传
     */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        //1.写入文件
        String crashFileName = saveInfoToSd(throwable);
        LogUtils.e(TAG,"fileName-->"+crashFileName);
        //2.缓存崩溃日志文件地址
        cacheCrashFile(crashFileName);
        //3.交由系统处理
        mDefaultUncaughtExceptionHandler.uncaughtException(thread,throwable);
    }

    /**
     * 缓存崩溃日志文件
     *
     * @param crashFileName
     */
    private void cacheCrashFile(String crashFileName) {
        SharedPreferences preferences = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        preferences.edit().putString(CRASH_FILE_NAME,crashFileName).commit();
    }

    /**
     * 获取崩溃文件
     * @return
     */
    public File getCrashFile(Context context){
        SharedPreferences preferences = context.getSharedPreferences("crash", Context.MODE_PRIVATE);
        String string = preferences.getString(CRASH_FILE_NAME, null);
        if (string != null)
            return new File(string);
        else
            return null;
    }

    /**
     * 保存获取的软件信息设备信息和出错信息到sd卡中
     *
     * @param throwable
     * @return
     */
    private String saveInfoToSd(Throwable throwable) {
        String fileName = null;

        //通过context获取手机信息和软件信息
        HashMap<String,String> simpleInfoMap = obtainSimpleInfo(mContext);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : simpleInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        //通过throwable获取异常信息
        String exceptionInfo = botainExceptionInfo(throwable);
        sb.append(exceptionInfo);

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(mContext.getFilesDir()+File.separator+"crash"+File.separator);
            // 先删除之前的异常信息
            if (dir.exists()){
                deleteDir(dir);
            }
            //重新创建文件夹
            if (!dir.exists()){
                dir.mkdir();
            }
            try{
                fileName = dir.toString()+File.separator+getAssignTime("yyyy_MM_dd_HH_mm")+".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 返回当前日期根据格式
     **/
    private String getAssignTime(String data) {
        DateFormat format = new SimpleDateFormat(data);
        long currentTimeMillis = System.currentTimeMillis();
        return format.format(currentTimeMillis);
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件 * * @param dir 将要删除的文件目录
     *
     * @return boolean Returns "true" if all deletions were successful.
     * If a * deletion fails, the method stops attempting to delete and returns * "false".
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile())
                    files[i].delete();
            }
        }
        return true;
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String botainExceptionInfo(Throwable throwable) {
        StringWriter  stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        writer.close();
        return stringWriter.toString();
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context mContext) {
        HashMap<String,String> map = new HashMap<>();
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            map.put("versionName",packageInfo.versionName);
            map.put("versionCode",packageInfo.versionCode+"");
            map.put("MODEL", Build.MODEL);
            map.put("SDK_INT", Build.VERSION.SDK_INT+"");
            map.put("PRODUCT", Build.PRODUCT);
            map.put("MOBILDE_INFO", getMoBileInfo());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Cell phone information
     * example:华为=h6
     * @return
     */
    private String getMoBileInfo() {
        StringBuffer buffer = new StringBuffer();
        try{
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                buffer.append(name+"="+value);
                buffer.append("\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
