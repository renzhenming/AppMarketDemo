package com.example.renzhenming.appmarket;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mylibrary.BaseSkinActivity;
import com.example.mylibrary.skin.SkinManager;
import com.example.renzhenming.appmarket.service.GuardService1;
import com.example.renzhenming.appmarket.service.GuardService2;
import com.example.renzhenming.appmarket.service.JobWakeUpService;

import java.io.File;

public class TestSkinActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_skin);
        startService(new Intent(getApplicationContext(), GuardService1.class));
        startService(new Intent(getApplicationContext(), GuardService2.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //必须大于5.0
            startService(new Intent(this,JobWakeUpService.class));
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void setContentView() {

    }


    public void skin(View view){
        // 从服务器上下载
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator +"red.skin";

        // 换肤
        int result = SkinManager.getInstance().loadSkin(skinPath);
    }

    public void skin1(View view){
        // 恢复默认
         SkinManager.getInstance().restoreDefault();
    }


    public void skin2(View view){
        // 跳转
        startActivity(new Intent(getApplicationContext(),TestSkinActivity.class));
    }

}
