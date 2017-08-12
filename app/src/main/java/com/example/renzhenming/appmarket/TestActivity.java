package com.example.renzhenming.appmarket;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rzm.commonlibrary.general.FixDexManager;
import com.rzm.commonlibrary.inject.BindViewId;
import com.rzm.commonlibrary.inject.CheckNet;
import com.rzm.commonlibrary.inject.OnClick;
import com.rzm.commonlibrary.inject.ViewBind;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    @BindViewId(R.id.click)
    TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ViewBind.inject(this);
        mText.setText("注入的值");



        fixDex();
    }

    private void fixDex() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fix.dex");
        if (file.exists()){
            FixDexManager manager = new FixDexManager(this);
            try {
                manager.fixDex(file.getAbsolutePath());
                Toast.makeText(getApplicationContext(),"修复bug成功",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"修复bug失败",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    @OnClick({R.id.click,R.id.click2})
    @CheckNet
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.click:
                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.click2:
                Toast.makeText(getApplicationContext(),"click2",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
