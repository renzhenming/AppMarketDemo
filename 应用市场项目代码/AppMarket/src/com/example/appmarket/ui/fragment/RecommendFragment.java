package com.example.appmarket.ui.fragment;

import java.util.ArrayList;

import com.example.appmarket.http.protocol.RecommendProtocol;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.ui.view.fly.ShakeListener;
import com.example.appmarket.ui.view.fly.ShakeListener.OnShakeListener;
import com.example.appmarket.ui.view.fly.StellarMap;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecommendFragment extends BaseFragment {
	

	private ArrayList<String> list;

	@Override
	public View oncreateSuccessView() {
		final StellarMap stellar = new StellarMap(UIUtils.getContext());
		stellar.setAdapter(new RecommendAdater());
		//设置x.y方向随机分布规则为9行8列
		stellar.setRegularity(9, 8);
		int padding = UIUtils.dp2px(10);
		//设置上下左右边距都为10dp
		stellar.setInnerPadding(padding, padding, padding, padding);
		//设置默认加载第一组数据，并且具有动画效果
		stellar.setGroup(0, true);
		
		//添加监听摇一摇效果
		ShakeListener listener = new ShakeListener(UIUtils.getContext());
		listener.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				stellar.zoomOut();
			}
		});
		return stellar;
	}

	@Override
	public LoadResult loadData() {
		RecommendProtocol pro = new RecommendProtocol();
		list = pro.getEntity(0);
		return check(list);
	}
	
	class RecommendAdater implements StellarMap.Adapter {

		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public int getCount(int group) {
			int count = list.size() / getGroupCount();
			//防止不能整除时漏掉部分数据，当到达最后一组的时候，将余数页加上
			if (group == getGroupCount() - 1) {
				count += list.size() % getGroupCount();
			}
			return count;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			position = position + getCount(group - 1) * group;
			String text = list.get(position);
			TextView view = new TextView(UIUtils.getContext());
			view.setText(text);
			//随机大小，范围是10-22
			int size = (int) (10 + Math.random()*12);
			view.setTextSize(size);
			//随机颜色50-200
			int r = (int) (50+Math.random()*150);
			int g = (int) (50+Math.random()*150);
			int b = (int) (50+Math.random()*150);
			view.setTextColor(Color.rgb(r, g, b));
			return view;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			
			if (isZoomIn) {
				//表示往下滑动，这时候我需要加载下一页数据
				group ++;
				if (group > getGroupCount() - 1) {
					//防止组数越界
					group = 0;
				}
			}else{
				//表示网上滑动,加载上一页数据
				group-- ;
				if (group < 0) {
					//防止越界
					group =  getGroupCount() - 1;
				}
			}
			return group;
		}
		
	}
}





















