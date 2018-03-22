package com.app.rzm.test;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.rzm.R;
import com.example.mylibrary.view.indicator.CommonIndicatorAdapter;
import com.example.mylibrary.view.indicator.CommonIndicatorView;
import com.example.mylibrary.view.indicator.RippleTextView;

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

    class TestCommonIndicatorAdapter extends CommonIndicatorAdapter {

        private final Context context;
        private final ArrayList<String> mList;
        private RippleTextView text;

        public TestCommonIndicatorAdapter(Context context, ArrayList<String> list) {
            this.context = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View getView(int position, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_indicator,parent,false);
            text = view.findViewById(R.id.item);
            text.setTextColor(Color.BLACK);
            text.setText(mList.get(position));
            return view;
        }

        @Override
        public void highLightIndicator(View indicatorView, float positionOffset) {
            super.highLightIndicator(indicatorView, positionOffset);
            RippleTextView right = indicatorView.findViewById(R.id.item);
            right.setDirection(RippleTextView.Direction.DIRECTION_LEFT);
            right.setCurrentProgress(positionOffset);
        }

      /*  @Override
        public void highLightIndicator(View indicatorView) {
            super.highLightIndicator(indicatorView);
            ((TextView)indicatorView.findViewById(R.id.item)).setTextColor(Color.RED);
        }*/

        @Override
        public void restoreIndicator(View indicatorView) {
            super.restoreIndicator(indicatorView);
            ((TextView)indicatorView.findViewById(R.id.item)).setTextColor(Color.BLACK);
        }

        /**
         * 如果不需要底部的指示器，是需要不重写这个方法即可
         * @return
         */
        @Override
        public View getBottomTrackView() {
            View view = new View(context);
            view.setBackgroundColor(Color.RED);
            view.setLayoutParams(new ViewGroup.LayoutParams(88,8));
            return view;
        }
    }
    class TestCommonIndicatorPagerAdapter extends PagerAdapter {
        private final ArrayList<String> mList;
        private final Context mContext;

        public TestCommonIndicatorPagerAdapter(ArrayList<String> mList, Context context) {
            this.mContext = context;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view = new TextView(mContext);
            view.setText(mList.get(position));
            view.setTextColor(Color.BLACK);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(20);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
