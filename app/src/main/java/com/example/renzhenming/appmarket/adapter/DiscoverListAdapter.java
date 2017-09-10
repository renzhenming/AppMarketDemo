package com.example.renzhenming.appmarket.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.example.mylibrary.view.indicator.recyclerview.adapter.CommonRecyclerAdapter;
import com.example.mylibrary.view.indicator.recyclerview.adapter.ViewHolder;
import com.example.renzhenming.appmarket.R;
import com.example.renzhenming.appmarket.bean.DiscoverListResult;
import com.example.renzhenming.appmarket.utils.GlideImageLoader;
import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/4/8.
 * Version 1.0
 * Description:
 */
public class DiscoverListAdapter extends
        CommonRecyclerAdapter<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> {
    public DiscoverListAdapter(Context context, List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> data) {
        super(context, data, R.layout.channel_list_item);
    }

    @Override
    public void convert(ViewHolder holder, DiscoverListResult.DataBean.CategoriesBean.CategoryListBean item) {
        // 显示数据
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        holder.setText(R.id.channel_text, item.getName())
                .setText(R.id.channel_topic, item.getIntro())
                .setText(R.id.channel_update_info, Html.fromHtml(str));

        // 是否是最新
        if (item.isIs_recommend()) {
            holder.setViewVisibility(R.id.recommend_label, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.recommend_label, View.GONE);
        }
        // 加载图片
        holder.setImageByUrl(R.id.channel_icon, new GlideImageLoader(item.getIcon_url()));
    }
}
