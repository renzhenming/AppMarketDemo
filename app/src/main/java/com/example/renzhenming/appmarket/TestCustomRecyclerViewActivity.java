package com.example.renzhenming.appmarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.mylibrary.view.indicator.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.indicator.recyclerview.view.LoadRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestCustomRecyclerViewActivity extends AppCompatActivity {

    private LoadRefreshRecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_recycler_view);
        initView();
    }

    private void initView() {
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recyclerview);
        for (int i = 0; i < 20; i++) {
            mList.add("测试数据"+i);
        }

        TestCommonAdapter adapter = new TestCommonAdapter(this, mList,new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position == 0){
                    return R.layout.item_head;
                }else{
                    return R.layout.item_center;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
