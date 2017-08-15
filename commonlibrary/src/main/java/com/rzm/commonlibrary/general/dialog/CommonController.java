package com.rzm.commonlibrary.general.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by renzhenming on 2017/8/14.
 */

public class CommonController {

    private Window mWindow;
    private CommonDialog mCommonDialog;

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
        //存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //dialog宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight= ViewGroup.LayoutParams.WRAP_CONTENT;
        //dialog位置
        public int mGravity = Gravity.CENTER;
        public int mAnimation = 0;


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

            //给dialog设置布局
            mAlert.getCommonDialog().setContentView(viewHelper.getContentView());
            //设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0 ; i < textArraySize;i++){
                viewHelper.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            //设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0 ; i < clickArraySize;i++){
                viewHelper.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }
            //配置自定义效果  全屏 底部弹出 默认动画
        }
    }
}
