package com.rzm.commonlibrary.stack.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.stack.mvp.view.BaseActivity;

/**
 * mvp BaseActivity及相关类使用方法
 */
public class MvpDemoActivity extends BaseActivity<MainView,MainPresenter> implements MainView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_demo);
    }

    public void login(View view){
        getPresenter().login("张三","123456");
    }

    @Override
    public void onLoginSuccess(String result) {
        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed(String ex) {

    }

    @Override
    protected MainView createView() {
        return this;
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }
}
