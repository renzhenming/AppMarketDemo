package com.rzm.commonlibrary.stack.presenter;

import com.rzm.commonlibrary.stack.view.IView;

/**
 * Created by renzhenming on 2017/8/5.
 */

public interface IPresenter<V extends IView> {

    /**绑定视图*/
    public void attachView(V view);

    /**解除绑定*/
    public void detachView();
}
