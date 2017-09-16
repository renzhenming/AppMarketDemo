package com.rzm.commonlibrary.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.rzm.commonlibrary.R;

/*findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        CommonDialog dialog = new CommonDialog.Builder(TestActivity.this)
        .setContentView(R.layout.dialog)
        .setText(R.id.toast,"我是新的dialog")
        .fullWidth()
        .alignBottom(true)
        .show();

        //我要获取到输入框的值，可以这样做 getView  (ListView RecyclerView CheckBox)
        // *//*final EditText mEditText = dialog.getView(输入框的id);
        dialog.setOnClickListener(R.id.toast, new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Toast.makeText(getApplicationContext(),mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
        }
        });*//*
}});*/

/**
 * Created by renzhenming on 2017/8/14.
 */

public class CommonDialog extends Dialog {

    private CommonController mAlert;

    public CommonDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mAlert = new CommonController(this,getWindow());
    }

    public static class Builder{
        private final CommonController.CommonParams P;

        public Builder(Context context) {
            this(context, R.style.default_dialog);
        }

        public Builder(Context context,int thmemId) {
            P = new CommonController.CommonParams(context,thmemId);
        }

        public Builder setContentView(View view){
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        public Builder setContentView(int layoutId){
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        //设置文本
        public Builder setText(int viewId,CharSequence text){
            P.mTextArray.put(viewId,text);
            return this;
        }
        //设置点击事件
        public Builder setOnClickListener(int viewId, View.OnClickListener listener){
            P.mClickArray.put(viewId,listener);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }


        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * 宽度占满界面
         * @return
         */
        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 在屏幕中的位置
         * @param isAnimation
         * @return
         */
        public Builder alignBottom(boolean isAnimation){
            if (isAnimation){
                P.mAnimation = R.style.ActionSheetDialogAnimation;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置宽高
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width,int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 默认动画
         * @return
         */
        public Builder setDefaultAnimation(){
            P.mAnimation = R.style.ActionSheetDialogAnimation;
            return this;
        }

        /**
         * 自定义动画
         * @param styleAnimation
         * @return
         */
        public Builder addAnimation(int styleAnimation){
            P.mAnimation = styleAnimation;
            return this;
        }

        /**
         * Creates an {@link CommonDialog} with the arguments supplied to this
         * build.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * build and display the dialog.
         */
        public CommonDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final CommonDialog dialog = new CommonDialog(P.mContext, P.mThemeId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link CommonDialog} with the arguments supplied to this
         * build and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = build.build();
         *     dialog.show();
         * </pre>
         */
        public CommonDialog show() {
            final CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    /////////////////////////////////为了获取输入框文字而添加的/////////////////////////////////////

    /**
     * 设置文本
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        mAlert.setText(viewId,charSequence);
    }

    /**
     * 点击事件
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnClickListener(viewId,listener);
    }

    /**
     * 获取view,先获取缓存，没有再findViewById
     * @param viewId
     * @return
     */
    public <T extends View>T getView(int viewId) {
        return mAlert.getView(viewId);
    }
}
