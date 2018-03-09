package com.app.rzm.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rzm.R;
import com.example.mylibrary.view.indicator2.CommonIndicatorAdapter;

import java.util.ArrayList;

/**
 * Created by renzhenming on 2018/3/9.
 */

public class TestCommonIndicatorAdapter extends CommonIndicatorAdapter {

    private final Context context;
    private final ArrayList<String> mList;
    private TextView text;

    public TestCommonIndicatorAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_indicator,parent,false);
        text = view.findViewById(R.id.item);
        text.setTextColor(Color.BLACK);
        text.setText(mList.get(position));
        return view;
    }

    @Override
    public void highLightIndicator(View indicatorView) {
        super.highLightIndicator(indicatorView);
        ((TextView)indicatorView.findViewById(R.id.item)).setTextColor(Color.RED);
    }

    @Override
    public void restoreIndicator(View indicatorView) {
        super.restoreIndicator(indicatorView);
        ((TextView)indicatorView.findViewById(R.id.item)).setTextColor(Color.BLACK);
    }
}
