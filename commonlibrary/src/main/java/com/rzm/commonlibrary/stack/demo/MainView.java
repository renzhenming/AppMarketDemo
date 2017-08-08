package com.rzm.commonlibrary.stack.demo;

import com.rzm.commonlibrary.stack.mvp.view.IView;

/**
 * Created by renzhenming on 2017/8/5.
 */

public interface MainView extends IView {
    void onLoginSuccess(String result);
    void onLoginFailed(String result);
}
