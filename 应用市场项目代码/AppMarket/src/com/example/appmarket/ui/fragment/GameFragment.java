package com.example.appmarket.ui.fragment;

import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameFragment extends BaseFragment {
	
	@Override
	public View oncreateSuccessView() {
		TextView view = new TextView(UIUtils.getContext());
		view.setText(getClass().getSimpleName());
		view.setGravity(Gravity.CENTER);
		view.setTextSize(20);
		view.setTextColor(Color.RED);
		return view;
	}

	@Override
	public LoadResult loadData() {
		return LoadResult.RESULT_ERROR;
	}
}
