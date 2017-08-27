package com.example.mylibrary;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rzm.commonlibrary.stack.mvc.BaseActivity;

/**
 * Created by renzhenming on 2017/8/8.
 * 为后期的插件换肤预留的一个类,项目中activity通过继承这个中间类来继承BaseActivity
 * tip : 永远在activity和BaseActivity中预留一层，用于未来迭代可能存在的扩展
 */

public abstract class BaseSkinActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(inflater, new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
                if (s.equals("Button")){
                    TextView view1  = new TextView(getApplicationContext());
                    view1.setText("Factory2 拦截view的创建");
                    return view1;
                }
                return null;
            }

            @Override
            public View onCreateView(String s, Context context, AttributeSet attributeSet) {
                return null;
            }
        });
        super.onCreate(savedInstanceState);

    }
}
