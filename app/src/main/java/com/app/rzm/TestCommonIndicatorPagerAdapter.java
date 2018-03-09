package com.app.rzm;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by renzhenming on 2018/3/9.
 */

public class TestCommonIndicatorPagerAdapter extends PagerAdapter {
    private final ArrayList<String> mList;
    private final Context mContext;

    public TestCommonIndicatorPagerAdapter(ArrayList<String> mList, Context context) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView view = new TextView(mContext);
        view.setText(mList.get(position));
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(20);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
