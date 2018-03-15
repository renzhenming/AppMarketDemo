package com.app.rzm.test;

import android.content.Context;

import com.app.rzm.R;
import com.example.mylibrary.view.recyclerview.adapter.CommonRecyclerAdapter;
import com.example.mylibrary.view.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.recyclerview.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by rzm on 2017/11/8.
 */

public class TestCommonAdapter extends CommonRecyclerAdapter<String> {
    public TestCommonAdapter(Context context, List<String> data, MultiTypeSupport<String> layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(CommonViewHolder holder, String item,int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == R.layout.item_center){
            CommonViewHolder viewHolder = holder;
            viewHolder.setText(R.id.center_text,item);
        }
    }
}
