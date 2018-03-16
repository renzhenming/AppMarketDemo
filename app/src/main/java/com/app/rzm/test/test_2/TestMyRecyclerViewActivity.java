package com.app.rzm.test.test_2;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.example.mylibrary.view.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.recyclerview2.adpter.CommonRecyclerAdpater;
import com.example.mylibrary.view.recyclerview2.creator.DefaultRefreshCreator;
import com.example.mylibrary.view.recyclerview2.view.RefreshRecyclerView;
import com.example.mylibrary.view.recyclerview2.view.RefreshViewCreator;
import com.example.mylibrary.view.recyclerview2.view.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestMyRecyclerViewActivity extends AppCompatActivity {

    private RefreshRecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_my_recyler_view);

        mRecyclerview = (RefreshRecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> mList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            mList.add("测试数据"+i);
        }

        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),mList, new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position %2 == 1){
                    return R.layout.item_center;
                }
                return R.layout.item_normal;
            }
        });
        myAdapter.setOnItemClickListener(new CommonRecyclerAdpater.OnItemClickListener<String>() {
            @Override
            public void onItemClick(String o, int position) {
                Toast.makeText(getApplicationContext(),o,Toast.LENGTH_SHORT).show();
            }
        });
        myAdapter.setOnLongClickListener(new CommonRecyclerAdpater.OnLongClickListener<String>() {
            @Override
            public void onItemLongClick(String o, int position) {
                Toast.makeText(getApplicationContext(),o,Toast.LENGTH_SHORT).show();
            }
        });

        myAdapter.setOnItemChildClickListener(R.id.text_adapter,new CommonRecyclerAdpater.OnItemChildClickListener<String>() {
            @Override
            public void onItemChildClick(String o, int position) {
                Toast.makeText(getApplicationContext(),"text,position="+position,Toast.LENGTH_SHORT).show();
            }
        });
        myAdapter.setOnItemChildClickListener(R.id.image_adapter,new CommonRecyclerAdpater.OnItemChildClickListener<String>() {
            @Override
            public void onItemChildClick(String o, int position) {
                Toast.makeText(getApplicationContext(),"image,position="+position,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerview.setAdapter(myAdapter);
        mRecyclerview.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerview.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerview.onStopRefresh();
                    }
                },1500);
            }
        });

        View header = LayoutInflater.from(this).inflate(R.layout.item_head,mRecyclerview,false);
        mRecyclerview.addHeaderView(header);

        View footer = LayoutInflater.from(this).inflate(R.layout.item_footer,mRecyclerview,false);
        mRecyclerview.addFooterView(footer);
    }

    class MyAdapter extends CommonRecyclerAdpater<String>{

        public MyAdapter(Context context, List mDataList, int resourseId) {
            super(context, mDataList, resourseId);
        }

        public MyAdapter(Context context, List<String> mDataList, MultiTypeSupport<String> multiTypeSupport) {
            super(context, mDataList, multiTypeSupport);
        }

        @Override
        public void bindHolder(ViewHolder holder, String text, int position) {
            holder.setText(R.id.text,text);

        }
    }
}
