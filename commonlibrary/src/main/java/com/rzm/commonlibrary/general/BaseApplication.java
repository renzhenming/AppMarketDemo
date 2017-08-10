package com.rzm.commonlibrary.general;

import android.app.Application;

import com.rzm.commonlibrary.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by renzhenming on 2017/8/10.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        File file = ExceptionCrashHandler.getInstance().getCrashFile(this);
        if (file != null){
            //上传上次崩溃文件到服务器
            LogUtils.e("crash_file","找到崩溃文件了");
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                char [] buff = new char[1024];
                int len = 0;
                while((len = reader.read(buff))!= -1){
                    String str = new String(buff,0,len);
                    LogUtils.d("tag",str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //初始化异常捕获
        ExceptionCrashHandler.getInstance().init(this);
    }
}
