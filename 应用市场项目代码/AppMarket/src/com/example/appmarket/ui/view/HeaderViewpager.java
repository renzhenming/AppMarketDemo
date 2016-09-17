package com.example.appmarket.ui.view;

import java.sql.Wrapper;
import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.appmarket.R;
import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class HeaderViewpager extends GlobalHolder<ArrayList<String>> {

	private BitmapUtils mBitmapUtils;
	private ViewPager pager;
	private LinearLayout pointLayout;

	@Override
	public View initView() {
		// 初始化跟布局
		RelativeLayout reLayout = new RelativeLayout(UIUtils.getContext());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dp2px(150));
		reLayout.setLayoutParams(params);
		// 初始化viewpager
		pager = new ViewPager(UIUtils.getContext());
		RelativeLayout.LayoutParams vParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		reLayout.addView(pager, vParams);

		// 初始化指示器布局
		pointLayout = new LinearLayout(UIUtils.getContext());
		RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		int padding = UIUtils.dp2px(5);
		pointLayout.setPadding(padding, padding, padding, padding);
		// 设置指示器布局位于右下角
		pointParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pointParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		reLayout.addView(pointLayout, pointParams);
		
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return reLayout;
	}

	@Override
	public void refreshData(final ArrayList<String> data) {
		pager.setAdapter(new ViewPagerAdapter(data));

		for (int i = 0; i < data.size(); i++) {
			ImageView view = new ImageView(UIUtils.getContext());
			LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			int margin = UIUtils.dp2px(3);
			imageParams.rightMargin = margin;

			if (i == 0) {
				view.setBackgroundResource(R.drawable.indicator_selected);
			}else{
				view.setBackgroundResource(R.drawable.indicator_normal);
			}
			pointLayout.addView(view,imageParams);
		}
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				arg0 = arg0 % data.size();
				for (int i = 0; i < data.size(); i++) {
					ImageView imageview = (ImageView) pointLayout.getChildAt(i);
					if (i == arg0) {
						imageview.setImageResource(R.drawable.indicator_selected);
					}else{
						imageview.setImageResource(R.drawable.indicator_normal);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		pager.setCurrentItem(data.size()*1000);
		new Task().start();
	}
	
	//轮播效果
	class Task implements Runnable{

		public void start(){
			UIUtils.getHandler().removeCallbacksAndMessages(null);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		@Override
		public void run() {
			int currentItem = pager.getCurrentItem();
			pager.setCurrentItem(++currentItem);
			UIUtils.getHandler().postDelayed(this, 3000);
		}
		
	}

	class ViewPagerAdapter extends PagerAdapter {

		private ArrayList<String> data;

		public ViewPagerAdapter(ArrayList<String> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % data.size();
			ImageView image = new ImageView(UIUtils.getContext());
			image.setScaleType(ScaleType.CENTER_CROP);
			mBitmapUtils.display(image,
					HttpHelper.URL + "image?name=" + data.get(position));
			container.addView(image);
			return image;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
