package com.example.mylibrary.view.recyclerview2.multi;

/**
 * Created by renzhenming on 2018/3/15.
 */

public interface MutiTypeSupport<R> {

    //返回的布局
    int getLayoutId(R data,int position);
}
