package com.example.appmarket.ui.fragment;

import java.util.ArrayList;

import com.example.appmarket.http.protocol.HotProtocol;
import com.example.appmarket.ui.view.FlowLayout;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.ui.view.fly.AnimationUtil;
import com.example.appmarket.utils.AnimationUtils;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class HotFragment extends BaseFragment {

	private ArrayList<String> list;
	
	@Override
	public View oncreateSuccessView() {
		
		int radius = UIUtils.dp2px(5);
		int padding = UIUtils.dp2px(10);
		ScrollView sview = new ScrollView(UIUtils.getContext());
		
		FlowLayout flayout = new FlowLayout(UIUtils.getContext());
		for (int i = 0; i < list.size(); i++) {
			String text = list.get(i);
			final TextView view = new TextView(UIUtils.getContext());
			view.setText(text);
			view.setTextColor(Color.WHITE);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			view.setGravity(Gravity.CENTER);
			view.setPadding(padding-2, padding-2, padding-2, padding-2);
			int r = (int) (50+Math.random()*150);
			int g = (int) (50+Math.random()*150);
			int b = (int) (50+Math.random()*150);
			//这个是默认的背景色
			GradientDrawable drawable = AnimationUtils.getGradientDrawable(Color.rgb(r, g, b), radius);
			
			//这个是按下后的背景色
			int pressedcolor = 0xffcecece;
			Drawable pressedDrawable = AnimationUtils.getGradientDrawable(pressedcolor, radius);
			StateListDrawable listDrawable = AnimationUtils.getStateListDrawable(pressedDrawable, drawable);
			view.setBackgroundDrawable(listDrawable);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), view.getText(), 0).show();
				}
			});
			
			flayout.setPadding(radius, radius, radius, radius);
			flayout.setHorizontalSpacing(padding);
			flayout.setVerticalSpacing(padding);
			flayout.addView(view);
			
		}
		sview.addView(flayout);
		return sview;
	}

	@Override
	public LoadResult loadData() {
		HotProtocol pro = new HotProtocol();
		list = pro.getEntity(0);
		return check(list);
	}
}
