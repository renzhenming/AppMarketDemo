package com.example.renzhenming.appmarket;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.example.mylibrary.view.indicator.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultLoadCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultRefreshCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.LoadRefreshRecyclerView;
import com.example.mylibrary.view.indicator.recyclerview.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestCustomRecyclerViewActivity extends AppCompatActivity {

    private LoadRefreshRecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private TestCommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_recycler_view);
        initView();
    }

    private void initView() {
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recyclerview);

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
        View view = findViewById(R.id.view_empty);
        View loadingView = findViewById(R.id.view_loading);


        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.onStopRefresh();
                    }
                },2000);
            }
        });
        mRecyclerView.addEmptyView(view);
        mRecyclerView.addLoadingView(loadingView);
        mRecyclerView.addFailureView(loadingView);
        mRecyclerView.setOnLoadMoreListener(new LoadRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.onStopLoad();
                        for (int i = 0; i < 20; i++) {
                            mList.add("测试数据"+i);
                        }

                        adapter.notifyDataSetChanged();
                    }
                },2000);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mList.add("测试数据"+i);
                }
                adapter.notifyDataSetChanged();
            }
        },2000);

    }
}
