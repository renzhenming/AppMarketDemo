package com.rzm.commonlibrary.stack;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.stack.presenter.IPresenter;
import com.rzm.commonlibrary.stack.view.IView;

/**
 * extends  this Activity to facilitate the management of multiple fragment instances
 * User: chengwangyong(cwy545177162@163.com)
 * Date: 2016-01-19
 * Time: 18:32
 */
public abstract class BaseActivity<V extends IView,P extends IPresenter<V>> extends AppCompatActivity {

    public StackManager manager;
    public KeyCallBack callBack;
    private P presenter;
    private V view;


    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化mvp架构相关
        initPresenter();
        //初始化fragment栈管理
        initFragmentStack(savedInstanceState);
        mOnCreate(savedInstanceState);
    }

    protected void mOnCreate(Bundle savedInstanceState){

    }

    protected void initFragmentStack(@Nullable Bundle savedInstanceState){
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.setId(R.id.framLayoutId);
        setContentView(frameLayout);
        BaseFragment fragment = getRootFragment();
        if (fragment != null){
            manager = new StackManager(this);
            manager.setFragment(fragment);
            onCreateNow(savedInstanceState);
        }
    }

    protected void initPresenter(){
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

    public V getView() {
        return view;
    }

    public P getPresenter() {
        return presenter;
    }

    /**
     * 如果使用到了mvp，重写这两个方法即可，否则不做处理
     * @return
     */
    protected V createView(){
        return null;
    }

    public P createPresenter() {
        return null;
    }

    /**
     * Set the bottom of the fragment
     *
     * @return fragment
     */
    public BaseFragment getRootFragment(){
        return null;
    }

    /**
     * Set page switch animation
     *
     * @param nextIn  The next page to enter the animation
     * @param nextOut The next page out of the animation
     * @param quitIn  The current page into the animation
     * @param quitOut Exit animation for the current page
     */
    public void setAnim(@AnimRes int nextIn, @AnimRes int nextOut, @AnimRes int quitIn, @AnimRes int quitOut) {
        manager.setAnim(nextIn, nextOut, quitIn, quitOut);
    }

    /**
     * Rewriting onCreate method
     *
     * @param savedInstanceState savedInstanceState
     */
    public void onCreateNow(Bundle savedInstanceState) {

    }


    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                manager.onBackPressed();
                return true;
            default:
                if (callBack != null) {
                    return callBack.onKeyDown(keyCode, event);
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Set button to click callback
     *
     * @param callBack callback
     */
    public void setKeyCallBack(KeyCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 解除presenter的绑定
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
    }

}
