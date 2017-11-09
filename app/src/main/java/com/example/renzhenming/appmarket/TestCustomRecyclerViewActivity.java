package com.example.renzhenming.appmarket;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mylibrary.view.indicator.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultLoadCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultRefreshCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.LoadRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestCustomRecyclerViewActivity extends AppCompatActivity {

    private LoadRefreshRecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private LinearLayout mContainer;
    private TestCommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_recycler_view);
        initView();
    }

    private void initView() {
        mContainer = (LinearLayout) findViewById(R.id.container);
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recyclerview);
        for (int i = 0; i < 20; i++) {
            mList.add("测试数据"+i);
        }

        adapter = new TestCommonAdapter(this, mList,new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position == 0){
                    return R.layout.item_head;
                }else{
                    return R.layout.item_center;
                }
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.view_empty,null,false);
        View loadingView = LayoutInflater.from(this).inflate(R.layout.view_loading,null,false);
        mRecyclerView.addEmptyView(view);
        mRecyclerView.addLoadingView(loadingView);
        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public void stop_refresh(View view) {
        mRecyclerView.onStopRefresh();
    }

    public void stop_load(View view) {
        mRecyclerView.onStopLoad();
    }

    public void load_finish(View view) {
        mRecyclerView.setAdapter(adapter);
    }
}
