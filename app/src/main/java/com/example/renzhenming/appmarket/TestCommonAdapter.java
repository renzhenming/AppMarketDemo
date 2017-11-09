package com.example.renzhenming.appmarket;

import android.content.Context;

import com.example.mylibrary.view.indicator.recyclerview.adapter.CommonRecyclerAdapter;
import com.example.mylibrary.view.indicator.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.indicator.recyclerview.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by renzhenming on 2017/11/8.
 */

public class TestCommonAdapter extends CommonRecyclerAdapter<String> {
    public TestCommonAdapter(Context context, List<String> data, MultiTypeSupport<String> layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, String item,int position) {
        if (position == 0)
            return;
        CommonViewHolder viewHolder = holder;
        viewHolder.setText(R.id.center_text,item);
    }
}
