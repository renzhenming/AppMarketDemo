package com.rzm.commonlibrary.stack.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.stack.BaseActivity;

public class MvpDemoActivity extends BaseActivity<MainView,MainPresenter> implements MainView{

    /**
     * 相当于系统的onCreate方法
     * @param savedInstanceState
     */
    @Override
    protected void mOnCreate(Bundle savedInstanceState) {
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

}
