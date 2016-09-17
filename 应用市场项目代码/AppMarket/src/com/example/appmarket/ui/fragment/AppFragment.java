package com.example.appmarket.ui.fragment;

import java.util.ArrayList;

import com.example.appmarket.R;
import com.example.appmarket.adapter.GlobalAdapter;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.holder.AppHolder;
import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.http.protocol.AppProtocol;
import com.example.appmarket.ui.fragment.HomeFragment.HomeAdapter;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AppFragment extends BaseFragment {

	private ArrayList<AppEntity> list;


	@Override
	public View oncreateSuccessView() {
		System.out.println("/////////////////////////////");
		View view = UIUtils.inflate(R.layout.app_list_view);
		ListView homeListview = (ListView) view
				.findViewById(R.id.home_listview);

		homeListview.setAdapter(new AppAdapter(list));
		return view;
	}

	@Override
	public LoadResult loadData() {
		System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

		AppProtocol pro = new AppProtocol();
		list = pro.getEntity(0);
		
		return check(list);
	}
	

	class AppAdapter extends GlobalAdapter<AppEntity> {

		public AppAdapter(ArrayList<AppEntity> list) {
			super(list);
		}

		@Override
		public GlobalHolder<AppEntity> getHolder(int position) {
			
			return new AppHolder();
		}

		@Override
		public ArrayList<AppEntity> loadMore() {
			AppProtocol pro = new AppProtocol();
			ArrayList<AppEntity> list = pro.getEntity(getList().size());
			return list;
		}

	}
}
