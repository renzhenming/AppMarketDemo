package com.app.rzm.service;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {


    private int jobId = 12;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启一个轮寻
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobId,new ComponentName(this,JobWakeUpService.class));
        //设置每两秒钟一次
        jobBuilder.setPeriodic(2000);
        jobScheduler.schedule(jobBuilder.build());
        Log.e("TAG", "开启一个轮寻JobWakeUpService");
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // 开启定时任务，定时轮寻 ， 看MessageService有没有被杀死
        // 如果杀死了启动  轮寻onStartJob

        // 判断服务有没有在运行
        boolean messageServiceAlive = serviceAlive(GuardService1.class.getName());
        if(!messageServiceAlive){
            Log.e("TAG", "GuardService1被杀掉了，重新启动");
            startService(new Intent(this,GuardService1.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    /**
     * 判断某个服务是否正在运行的方法
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
