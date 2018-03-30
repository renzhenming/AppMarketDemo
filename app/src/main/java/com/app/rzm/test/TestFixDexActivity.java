package com.app.rzm.test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.general.FixDexManager;

import java.io.File;

public class TestFixDexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fix_dex);
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

    public void bump(View view) {
        int a = 2/0;
    }

    public void fixBump(View view) {
        fixDex();
    }
}
