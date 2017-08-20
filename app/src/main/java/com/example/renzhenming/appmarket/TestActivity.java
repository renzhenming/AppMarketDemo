package com.example.renzhenming.appmarket;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.HttpCallBack;
import com.rzm.commonlibrary.general.FixDexManager;
import com.rzm.commonlibrary.general.dialog.CommonDialog;
import com.rzm.commonlibrary.general.http.HttpUtils;
import com.rzm.commonlibrary.general.navigationbar.CommonNavigationBar;
import com.rzm.commonlibrary.general.navigationbar.StatusBarManager;
import com.rzm.commonlibrary.inject.BindViewId;
import com.rzm.commonlibrary.inject.ViewBind;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    @BindViewId(R.id.click)
    TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new StatusBarManager.builder(this)
                .setTintType(StatusBarManager.TintType.PURECOLOR)
                .setStatusBarColor(R.color.colorPrimary)
                .build();
        CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this)
                .setToolbarEnable(true)
                .setTitle("个人中心")
                .setRightIcon(R.drawable.search)
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"编辑",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog dialog = new CommonDialog.Builder(TestActivity.this)
                        .setContentView(R.layout.dialog)

                        .setText(R.id.toast,"我是新的dialog")
                        .fullWidth()
                        .alignBottom(true)
                        .show();
                //我要获取到输入框的值，可以这样做 getView  (ListView RecyclerView CheckBox)
                /*final EditText mEditText = dialog.getView(输入框的id);
                dialog.setOnClickListener(R.id.toast, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });

        //路径url参数都需要放到jni中，防止反编译被盗取到url
        HttpUtils httpUtils = HttpUtils.with(this)
                .get().url("http://www.baidu.com")
                .addParams("name","zhangsan")
                .addParams("sex","man")
                .execute(new HttpCallBack<String>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            public void onError(Exception e) {

            }
            @Override
            public void onSuccess(String result) {

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
