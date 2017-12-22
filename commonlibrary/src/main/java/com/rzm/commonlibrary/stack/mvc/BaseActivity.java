package com.rzm.commonlibrary.stack.mvc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rzm.commonlibrary.inject.ViewBind;

/**
 * Created by rzm on 2017/8/8.
 * 基于模板设计模式的基类，以传统mvc为架构
 *
 * BaseActivity中只能放一些通用的方法，基本每个activity都要使用的方法，比如类似数据库
 * 操作的readDataBase最好不要放进来，如果是两个以上的地方要使用，最好写成工具类的形式
 * 这样做主要是考虑性能问题，如果把一些不常使用的方法写在这里，这些方法就会常驻内存，
 *
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView();
        // 加入注解
        ViewBind.inject(this);
        //设置导航栏，toolbar或者actionbar之类的
        initTitle();
        //初始化view
        initView();
        //初始化数据
        initData();
    }

    /**
     * 启动activity
     * @param clazz
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    /**
     * findViewById查找view，直接调用可以避免强转
     * @param viewId
     * @param <R>
     * @return
     */
    protected <R extends View> R getViewById(int viewId){
        return (R)findViewById(viewId);
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initTitle();

    public abstract void setContentView();
}
