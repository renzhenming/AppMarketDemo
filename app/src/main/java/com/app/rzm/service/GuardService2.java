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

/**
 * START_NOT_STICKY：表示当Service运行的进程被Android系统强制杀掉之后，不会重新创建该Service，如果想重新实例化该Service，就必须重新调用startService来启动。
        使用场景：表示当Service在执行工作中被中断几次无关紧要或者对Android内存紧张的情况下需要被杀掉且不会立即重新创建这种行为也可接受的话，这是可以在onStartCommand返回值中设置该值。如在Service中定时从服务器中获取最新数据

 START_STICKY：表示Service运行的进程被Android系统强制杀掉之后，Android系统会将该Service依然设置为started状态（即运行状态），但是不再保存onStartCommand方法传入的intent对象，然后Android系统会尝试再次重新创建该Service，并执行onStartCommand回调方法，这时onStartCommand回调方法的Intent参数为null，也就是onStartCommand方法虽然会执行但是获取不到intent信息。
        使用场景：如果你的Service可以在任意时刻运行或结束都没什么问题，而且不需要intent信息，那么就可以在onStartCommand方法中返回START_STICKY，比如一个用来播放背景音乐功能的Service就适合返回该值。

 START_REDELIVER_INTENT：表示Service运行的进程被Android系统强制杀掉之后，与返回START_STICKY的情况类似，Android系统会将再次重新创建该Service，并执行onStartCommand回调方法，但是不同的是，Android系统会再次将Service在被杀掉之前最后一次传入onStartCommand方法中的Intent再次保留下来并再次传入到重新创建后的Service的onStartCommand方法中，这样我们就能读取到intent参数。
        使用场景：如果我们的Service需要依赖具体的Intent才能运行（需要从Intent中读取相关数据信息等），并且在强制销毁后有必要重新创建运行，那么这样的Service就适合返回START_REDELIVER_INTENT。
 */
public class GuardService2 extends Service {
    private final int GuardServiceId1 = 1;
    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Log.e("TAG", "GuardService2等待接收消息");
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
        startForeground(GuardServiceId1,new Notification());
        //绑定建立链接
        bindService(new Intent(getApplicationContext(),GuardService1.class),connection, Context.BIND_IMPORTANT);
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
            startService(new Intent(getApplicationContext(), GuardService1.class));
            bindService(new Intent(getApplicationContext(),GuardService1.class),connection, Context.BIND_IMPORTANT);
            Log.e("TAG", "与GuardService1的链接断开，重新启动服务并绑定");
        }
    };
}
