package com.app.rzm.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.renzhenming.appmarket.test.GuardAidl;

public class GuardService1 extends Service {
    private final int GuardServiceId2 = 2;
    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Log.e("TAG", "GuardService1等待接收消息");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //提高进程优先级
        startForeground(GuardServiceId2,new Notification());
        //绑定建立链接
        bindService(new Intent(getApplicationContext(),GuardService2.class),connection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GuardAidl.Stub(){};
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(getApplicationContext(),"建立链接",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开链接 ,重新启动，重新绑定
            startService(new Intent(getApplicationContext(), GuardService2.class));
            bindService(new Intent(getApplicationContext(),GuardService2.class),connection, Context.BIND_IMPORTANT);
            Log.e("TAG", "与GuardService2的链接断开，重新启动服务并绑定");
        }
    };
}
