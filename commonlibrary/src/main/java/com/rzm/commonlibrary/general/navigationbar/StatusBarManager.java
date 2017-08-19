package com.rzm.commonlibrary.general.navigationbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import static com.rzm.commonlibrary.general.navigationbar.StatusBarView.addStatusBarView;

/**
 * Created by chenyk on 2016/10/11.
 * 状态栏管理类
 * 使用方法
 * new StatusBarManager.builder(this)
 *     .setStatusBarColor(mStatusBarColor)//状态栏颜色
 *     .setTintType(mTintType)//色彩类型：纯色、渐变
 *     .setAlpha(mAlpha)//不透明度
 *     .create();
 */
public class StatusBarManager {
    private Activity mActivity;
    private TintType mTintType;   //色彩类型
    private int mAlpha;  //透明度值
    private int mStatusBarColor;   //状态栏颜色

    public enum TintType {
        GRADIENT, PURECOLOR  //渐变，纯色   ps:纯色效果仅适用于android 5.0以上
    }

    /**
     * 构造函数
     *  @param activity
     * @param tintType
     * @param alpha
     * @param statusBarColor
     */
    private StatusBarManager(Activity activity, TintType tintType, int alpha, int statusBarColor) {
        this.mActivity = activity;
        this.mTintType = tintType;
        this.mAlpha = alpha;
        this.mStatusBarColor = getResultColor(mActivity, statusBarColor);
        windowConfig();
    }

    /**
     * 窗口相关配置
     * 4.4以上需要自己绘制一个与状态栏同高的矩形条，并通过addview的方法添加至屏幕上，
     * 同时设置其背景颜色达到预期的效果。而5.0以上api中提供了setStatusBarColor（int color）
     * 的方法可直接设置状态的颜色
     */
    private void windowConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            if (TintType.PURECOLOR == mTintType) {
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mActivity.getWindow().setStatusBarColor(calculateColorWithAlpha(mStatusBarColor, mAlpha));//设置状态栏颜色
            } else if (TintType.GRADIENT == mTintType) {
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                addStatusBarView(mActivity, calculateColorWithAlpha(mStatusBarColor, mAlpha));
            }
            //透明底部导航栏
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusBarView(mActivity, calculateColorWithAlpha(mStatusBarColor, mAlpha));
        }
    }

    /**
     * builder配置类
     */
    public static class builder {
        private static final int DEFAULT_ALPHA = 60;  //默认透明度数值
        private Activity activity;
        private TintType tintType = TintType.PURECOLOR;  //默认纯色效果
        private int alpha = DEFAULT_ALPHA;
        private int statusBarColor;

        /**
         * 构造方法
         *
         * @param activity
         */
        public builder(Activity activity) {
            this.activity = activity;
        }

        /**
         * 设置色彩类型
         *
         * @param tintType
         * @return
         */
        public builder setTintType(TintType tintType) {
            this.tintType = tintType;
            return this;
        }

        /**
         * 设置透明度
         *
         * @param alpha
         * @return
         */
        public builder setAlpha(int alpha) {
            if (alpha >= 0 & alpha <= 255)
                this.alpha = alpha;
            return this;
        }

        /**
         * 设置状态栏背景颜色
         *
         * @param StatusBarColor
         * @return
         */
        public builder setStatusBarColor(int StatusBarColor) {
            this.statusBarColor = StatusBarColor;
            return this;
        }

        /**
         * 创建StatusBarManager对象并返回
         *
         * @return
         */
        public StatusBarManager build() {
            return new StatusBarManager(activity, tintType, alpha, statusBarColor);
        }
    }

    //------------------------------   工具   -----------------------------------------//

    /**
     * 获取颜色值
     *
     * @param activity
     * @param object
     * @return
     */
    public static int getResultColor(Activity activity, Object object) {
        if (object instanceof String) {// "#666666"
            return Color.parseColor((String) object);
        } else if (object instanceof Integer) {
            if ((Integer) object > 0)// R.string.app_color
                return ContextCompat.getColor(activity,(Integer) object);
            else return (Integer) object;// Color.BLUE
        } else throw new IllegalStateException("The current color is not found");
    }

    /**
     * 获取文本值
     *
     * @param activity
     * @param object
     * @return
     */
    public static String getResultString(Activity activity, Object object) {
        if (object instanceof String)//"标题"
            return (String) object;
        else if (object instanceof Integer && (Integer) object > 0) //R.string.app_name
            return activity.getResources().getString((Integer) object);
        else throw new IllegalStateException("The current string is not found");
    }

    /**
     * 计算颜色值
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终颜色
     */
    public static int calculateColorWithAlpha(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

}
