package com.rzm.commonlibrary.utils;

import android.content.Context;

/**
 * Created by rzm on 2017/7/22.
 */

public class DimenUtil {
    //dp转换成px
    public static int dp2px(Context context, int dp){
        return (int)(dp * context.getResources().getDisplayMetrics().density + 0.5);
    }
    public static int px2dp(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
}