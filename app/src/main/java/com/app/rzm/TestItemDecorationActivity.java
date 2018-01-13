package com.app.rzm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mylibrary.view.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.recyclerview.itemdecoration.AlphabetItemDecoration;
import com.example.mylibrary.view.recyclerview.view.LoadRefreshRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestItemDecorationActivity extends AppCompatActivity {

    private LoadRefreshRecyclerView mRecyclerView;
    private TestCommonAdapter mAdapter;
    private List<String> mList;
    private List<Character> characterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_item_decoration);

        mList = new ArrayList<>();
        mList.add("provide");
        mList.add("continue");
        mList.add("name");
        mList.add("every");
        mList.add("center");
        mList.add("live");
        mList.add("unite");
        mList.add("power");
        mList.add("pay");
        mList.add("result");
        mList.add("member");
        mList.add("woman");
        mList.add("almost");
        mList.add("enough");
        mList.add("young");
        mList.add("though");
        mList.add("business");
        mList.add("value");
        mList.add("clear");
        mList.add("expect");
        mList.add("family");
        mList.add("experience");
        mList.add("direct");
        mList.add("industry");
        mList.add("important");
        mList.add("several");
        mList.add("god");
        mList.add("girl");
        mList.add("simple");
        mList.add("care");
        mList.add("foot");
        mList.add("understand");
        mList.add("probable");
        mList.add("particular");
        mList.add("today");
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new AlphabetItemDecoration(this, new AlphabetItemDecoration.DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                char c = Character.toUpperCase(mList.get(position).charAt(0));
                return c;
            }

            @Override
            public String getGroupFirstLine(int position) {
                return mList.get(position).substring(0, 1).toUpperCase();
            }
        }));
        mAdapter = new TestCommonAdapter(this, mList, new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                return R.layout.item_center;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
