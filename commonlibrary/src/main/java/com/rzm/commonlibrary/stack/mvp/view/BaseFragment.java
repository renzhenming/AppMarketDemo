package com.rzm.commonlibrary.stack.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.rzm.commonlibrary.stack.mvp.presenter.IPresenter;

/**
 * Created by rzm on 2017/8/5.
 */

public abstract class BaseFragment<V extends IView,P extends IPresenter<V>> extends Fragment {

    private P presenter;
    private V view;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter == null){
            presenter = createPresenter();
        }
        if (view == null){
            view = createView();
        }
        if (presenter != null && view != null){
            presenter.attachView(view);
        }
    }

    protected abstract V createView();

    public abstract P createPresenter() ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
    }
}
