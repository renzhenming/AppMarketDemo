package com.rzm.commonlibrary.stack.mvp.presenter;

import android.content.Context;

import com.rzm.commonlibrary.stack.mvp.view.IView;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by rzm on 2017/8/5.
 */

public class BasePresenter<V extends IView> implements IPresenter<V> {

    private final WeakReference<Context> weakContext;
    private WeakReference<V> weakView;
    private V proxyView;

    public BasePresenter(Context context) {
        this.weakContext = new WeakReference<Context>(context);
    }

    public Context getContext() {
        if (weakContext == null)
            return null;
        return weakContext.get();
    }

    public V getView() {
        return proxyView;
    }

    public boolean isAttachView(){
        if (weakView != null && weakView.get() != null){
            return true;
        }
        return false;
    }

    @Override
    public void attachView(V view) {
        this.weakView = new WeakReference<V>(view);
        ViewInvocationHandler invocationHandler = new ViewInvocationHandler(weakView.get());
        proxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), invocationHandler);
    }

    private class ViewInvocationHandler implements InvocationHandler{

        private final V view;

        public ViewInvocationHandler(V v) {
            this.view = v;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if (isAttachView()){
                return method.invoke(view,objects);
            }
            return null;
        }
    }

    @Override
    public void detachView() {
        if (weakView != null){
            weakView.clear();
            weakView = null;
        }
    }
}
