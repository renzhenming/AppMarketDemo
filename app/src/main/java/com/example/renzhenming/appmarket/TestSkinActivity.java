package com.example.renzhenming.appmarket;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mylibrary.skin.SkinManager;

import java.io.File;

public class TestSkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_skin);
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
        int result = SkinManager.getInstance().restoreDefault();
    }


    public void skin2(View view){
        // 跳转
        startActivity(new Intent(getApplicationContext(),TestSkinActivity.class));
    }

}
