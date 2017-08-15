package com.example.renzhenming.appmarket;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rzm.commonlibrary.general.FixDexManager;
import com.rzm.commonlibrary.general.dialog.CommonDialog;
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
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog dialog = new CommonDialog.Builder(TestActivity.this)
                        .setContentView(R.layout.dialog)
                        .setOnClickListener(R.id.toast, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"dialog",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        ViewBind.inject(this);
        mText.setText("注入的值");


        /**
         * 在这里修复遇到一个问题，就是第一次启动无法达到修复的效果，只有再次启动才可以，我想大概
         * 是调用的时机不对，于是把这行代码放在了application中，果然修复成功，目前这个问题不确定是不是
         * 机型的问题，因为别人好像在activity中调用修复就没问题
         */
       // fixDex();
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

}
