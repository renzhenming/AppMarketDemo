package com.rzm.commonlibrary.general.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.rzm.commonlibrary.R;


/**
 * Created by renzhenming on 2017/8/14.
 */

public class CommonController {

    private Window mWindow;
    private CommonDialog mCommonDialog;
    private CommonViewHelper mViewHelper;

    public CommonController(CommonDialog commonDialog, Window window) {
        this.mCommonDialog = commonDialog;
        this.mWindow = window;
    }

    public Window getWindow() {
        return mWindow;
    }

    public CommonDialog getCommonDialog() {
        return mCommonDialog;
    }

    public static class CommonParams{

        public Context mContext;
        public int mThemeId;
        //点击空白是否能够取消
        public boolean mCancelable = true;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog key监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;
        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放字体的修改
        public SparseArray<Integer> mGravityArray = new SparseArray<>();
        //存放view的显示隐藏状态
        public SparseArray<Integer> mViewVisible = new SparseArray<>();
        //存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //dialog宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight= ViewGroup.LayoutParams.WRAP_CONTENT;
        //dialog位置
        public int mGravity = Gravity.CENTER;
        //动画效果
        public int mAnimation = R.style.ActionSheetDialogAnimation;
        //占满全屏，隐藏状态栏
        public boolean mFullScreen = false;
        //设置透明度
        public float mAlpha = 1;


        public CommonParams(Context context, int thmemId) {
            this.mContext = context;
            this.mThemeId = thmemId;
        }

        /**
         * 绑定和设置参数
         * @param mAlert
         */
        public void apply(CommonController mAlert) {
            //设置布局 CommonHelper
            CommonViewHelper viewHelper = null;
            if (mViewLayoutResId !=0){
                viewHelper = new CommonViewHelper(mContext,mViewLayoutResId);
            }
            //设置文本
            if (mView != null){
                viewHelper = new CommonViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null){
                try {
                    throw new IllegalAccessException("haven't set a contentview");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            //设置背景透明度
            if(mView != null && mAlpha != 1){
                mView.setAlpha(mAlpha);
            }else{
                viewHelper.getContentView().setAlpha(mAlpha);
            }
            //给dialog设置布局
            mAlert.getCommonDialog().setContentView(viewHelper.getContentView());

            /////////////////////////////////为了获取输入框文字而添加的/////////////////////////////////////
            //设置辅助类
            mAlert.setViewHelper(viewHelper);

            /////////////////////////////////为了获取输入框文字而添加的/////////////////////////////////////


            //设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0 ; i < textArraySize;i++){
                //viewHelper.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
                //为了获取输入框文字而修改的
                mAlert.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            //设置gravity
            int gravityArraySize = mGravityArray.size();
            for (int i = 0 ; i < gravityArraySize;i++){
                mAlert.setGravity(mTextArray.keyAt(i),mGravityArray.valueAt(i));
            }

            //设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0 ; i < clickArraySize;i++){
                //viewHelper.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
                //为了获取输入框文字而修改的
                mAlert.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }

            //设置显示隐藏
            int viewVisibleSize = mViewVisible.size();
            for (int i = 0 ; i < viewVisibleSize;i++){
                //viewHelper.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
                //为了获取输入框文字而修改的
                mAlert.setVisibility(mViewVisible.keyAt(i),mViewVisible.valueAt(i));
            }


            //配置自定义效果  全屏 底部弹出 默认动画

            Window window = mAlert.getWindow();

            //设置弹出位置
            window.setGravity(mGravity);
            //设置动画
            if (mAnimation != 0){
                window.setWindowAnimations(mAnimation);
            }
            //设置宽高
            WindowManager.LayoutParams attributes = window.getAttributes();
            //是否全屏显示
            if (mFullScreen)
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            attributes.width = mWidth;
            attributes.height = mHeight;
            window.setAttributes(attributes);
        }
    }


    /////////////////////////////////为了获取输入框文字而添加的/////////////////////////////////////


    public void setViewHelper(CommonViewHelper mViewHelper) {
        this.mViewHelper = mViewHelper;
    }

    /**
     * 设置文本
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        mViewHelper.setText(viewId,charSequence);
    }
    /**
     * 设置gravity
     */
    public void setGravity(int viewId,int gravity){
        mViewHelper.setGravity(viewId,gravity);
    }

    /**
     * 点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId,listener);

    }


    public void setVisible(int viewId, int visible) {
        mViewHelper.setVisibility(viewId,visible);
    }



    private void setVisibility(int viewId, Integer integer) {
        mViewHelper.setVisibility(viewId,integer);
    }

    /**
     * 获取view,先获取缓存，没有再findViewById
     * @param viewId
     * @return
     */
    public <T extends View>T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }
}
