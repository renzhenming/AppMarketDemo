package com.example.renzhenming.appmarket;

import android.content.Context;
import android.view.View;

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
    public void convert(CommonViewHolder holder, String item) {
        TestCommonViewHolder viewHolder = (TestCommonViewHolder) holder;
        viewHolder.setText(R.id.center_text,item);
    }

    class TestCommonViewHolder extends CommonViewHolder{

        public TestCommonViewHolder(View itemView) {
            super(itemView);
        }
    }
}
