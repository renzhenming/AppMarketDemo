package com.rzm.commonlibrary.general;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.rzm.commonlibrary.utils.AppUtils;
import com.rzm.commonlibrary.utils.LogUtils;
import com.rzm.commonlibrary.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by rzm on 2017/8/10.
 */

public class BaseApplication extends Application {

    private PatchManager mPatchManger;

    @Override
    public void onCreate() {
        super.onCreate();
        //initExceptionHandler();
        //initAlibabaHotFix();
        //initMyHotFix();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    private void initMyHotFix() {
        FixDexManager manager = new FixDexManager(this);
        //加载所有修复的dex包，第一次的时候会从服务器上下载到修复的dex包并放在我们制定的目录下，
        //第二次进入的时候，直接读取保存的文件进行修复
        try {
            manager.loadFixDex();

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fix.dex");
            if (file.exists()){
                try {
                    manager.fixDex(file.getAbsolutePath());
                    Toast.makeText(getApplicationContext(),"修复bug成功",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"修复bug失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initAlibabaHotFix() {
        //初始化阿里热修复
        mPatchManger = new PatchManager(this);
        //获取当前应用版本
        mPatchManger.init(AppUtils.getVersionName(this));
        mPatchManger.loadPatch();

        //以下代码可以写在项目的application中

        //获取下载到的patch包
        File patchFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fix.apatch");
        if (patchFile != null){
            try {
                mPatchManger.addPatch(patchFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"修复失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initExceptionHandler() {
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
