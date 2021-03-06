package com.app.rzm.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.app.rzm.R;
import com.rzm.commonlibrary.general.navigationbar.impl.CommonNavigationBar;
import com.rzm.commonlibrary.general.navigationbar.impl.ToolbarStyleNavigationBar;

public class TestCommonNavigationBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_common_navigation_bar);
        CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this)
                .setTitle("个人中心")
                .setTitleTextColor(R.color.red)
                .setTitleTextSize(20)
                .setBackgroundColor(R.color.blue)
                .setRightIcon(R.drawable.btn_send)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"退出",Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"编辑",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        ToolbarStyleNavigationBar toolbarNavigationBar = new ToolbarStyleNavigationBar.Builder(this)
                .setTitle("测试标题")
                .setTitleTextColor(Color.BLUE)
                .setTitleTextSize(20)
                .setRightText("编辑")
                .setRightTextColor(Color.RED)
                .setRightTextSize(16)
                .setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"right",Toast.LENGTH_SHORT).show();
                    }
                })
                .setBackgroundColor(R.color.green)
                .build();
    }
}
