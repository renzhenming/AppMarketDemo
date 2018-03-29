package com.app.rzm.test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.inject.BindViewId;
import com.rzm.commonlibrary.inject.CheckNet;
import com.rzm.commonlibrary.inject.OnClick;
import com.rzm.commonlibrary.inject.ViewBind;

public class TestIOCViewBindActivity extends AppCompatActivity {

    @BindViewId(R.id.ioc)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_iocview_bind);

        ViewBind.inject(this);
        mTextView.setText("IOC绑定事件成功");
    }

    @OnClick(R.id.ioc)
    @CheckNet
    private void iocClick(View view){
        Toast.makeText(getApplicationContext(),"关闭网络,你就会看到我是如何处理无网络情况的～",Toast.LENGTH_SHORT).show();
    }

}
