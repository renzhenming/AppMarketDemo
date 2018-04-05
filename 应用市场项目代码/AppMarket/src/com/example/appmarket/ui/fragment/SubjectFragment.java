package com.example.appmarket.ui.fragment;

import java.text.Normalizer;
import java.util.ArrayList;

import com.example.appmarket.adapter.GlobalAdapter;
import com.example.appmarket.entity.SubjectEntity;
import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.holder.SubjectHolder;
import com.example.appmarket.http.protocol.SubjectProtocol;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.ui.view.NormalListView;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubjectFragment extends BaseFragment {
	

	private ArrayList<SubjectEntity> list;
	@Override
	public View oncreateSuccessView() {
		
		NormalListView view = new NormalListView(UIUtils.getContext());
		view.setAdapter(new SubjectAdapter(list));
		return view;
	}

	@Override
	public LoadResult loadData() {
		SubjectProtocol pro = new SubjectProtocol();
		list = pro.getEntity(0);
		return check(list);
	}
	class SubjectAdapter extends GlobalAdapter<SubjectEntity>{

		public SubjectAdapter(ArrayList<SubjectEntity> list) {
			super(list);
		}

		@Override
		public GlobalHolder<SubjectEntity> getHolder(int position) {
			SubjectHolder holder = new SubjectHolder();
			
			return holder;
		}

		@Override
		public ArrayList<SubjectEntity> loadMore() {
			SubjectProtocol pro = new SubjectProtocol();
			ArrayList<SubjectEntity> list = pro.getEntity(getList().size());
			return list;
		}
		
	}
}
