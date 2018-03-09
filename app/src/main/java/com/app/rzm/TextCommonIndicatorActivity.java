package com.app.rzm;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.rzm.utils.TestCommonIndicatorAdapter;
import com.example.mylibrary.view.indicator2.CommonIndicatorView;

import java.util.ArrayList;

public class TextCommonIndicatorActivity extends AppCompatActivity {

    private CommonIndicatorView mIndicatorView;
    private TestCommonIndicatorAdapter mAdapter;
    private ViewPager mViewPager;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_common_indicator);

        mList.add("推荐");
        mList.add("经济");
        mList.add("军事");
        mList.add("国际");
        mList.add("政治");
        mList.add("女人");
        mList.add("体育");
        mList.add("购物");
        mList.add("商场");
        mList.add("汽车");
        mList.add("飞机");
        mList.add("段子");
        mList.add("视频");
        mList.add("精华");
        mList.add("图片");


        mIndicatorView = (CommonIndicatorView) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mAdapter = new TestCommonIndicatorAdapter(this,mList);
        mIndicatorView.setAdapter(mAdapter,mViewPager);

        TestCommonIndicatorPagerAdapter pagerAdapter = new TestCommonIndicatorPagerAdapter(mList,this);
        mViewPager.setAdapter(pagerAdapter);
    }
}
