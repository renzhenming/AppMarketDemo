package com.rzm.commonlibrary.stack.demo;

import android.os.SystemClock;

/**
 * Created by rzm on 2017/8/5.
 */

public class MainModel {

    public void login(String username, String password, MainView listener){
        SystemClock.sleep(1000);
        if (true){
            listener.onLoginSuccess("登陆成功返回");
        }else {
            listener.onLoginFailed("登陆失败");
        }
    }
}
