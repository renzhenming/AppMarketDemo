package com.rzm.commonlibrary.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.recyclerview.ItemDecoration.DividerDecoration;

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
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
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
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public CommonDialog show() {
            final CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
