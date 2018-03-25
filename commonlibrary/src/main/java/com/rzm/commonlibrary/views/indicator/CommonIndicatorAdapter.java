package com.rzm.commonlibrary.views.indicator;


import android.view.View;
import android.view.ViewGroup;

/**
 * Created by renzhenming on 2018/3/9.
 *
 * Indicator适配器
 */

public abstract class CommonIndicatorAdapter<R extends View> {
    // 获取总的条数
    public abstract int getCount();

    /**
     * 根据当前的位置获取View，这个view指的是tab文字，或者文字加menu的形式
     */
    public abstract View getView(int position, ViewGroup parent);

    /**
     * 这两个方法没必要设置为抽象，应为并非所有的都要设置高亮状态
     * @param indicatorView
     */
    // 高亮当前被选中的item位置
    public void highLightIndicator(R indicatorView){

    }

    // 重置当前脱离选中位置的item
    public void restoreIndicator(R indicatorView){

    }

    /**
     * 这个view是指文字下方的指示器，一般是一条横线
     * @return
     */
    public View getBottomTrackView() {
        return null;
    }
}
