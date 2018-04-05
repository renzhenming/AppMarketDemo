package com.example.appmarket.ui.fragment;

import java.util.ArrayList;

import com.example.appmarket.R;
import com.example.appmarket.adapter.GlobalAdapter;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.holder.HomeHolder;
import com.example.appmarket.http.protocol.HomeProtocol;
import com.example.appmarket.ui.activity.AppDetailActivity;
import com.example.appmarket.ui.view.HeaderViewpager;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.utils.UIUtils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends BaseFragment implements OnItemClickListener {
	
	private ArrayList<AppEntity> homeList = null;
	private ArrayList<String> pictures;
	@Override
	public View oncreateSuccessView() {
		View view = View.inflate(getActivity(), R.layout.home_list_view, null);
		ListView homeListview = (ListView) view.findViewById(R.id.home_listview);
		HeaderViewpager pager = new HeaderViewpager();
		homeListview.addHeaderView(pager.getConvertView());
		pager.setData(pictures);
		homeListview.setAdapter(new HomeAdapter(homeList));
		
		homeListview.setOnItemClickListener(this);
		return view;
	}

	@Override
	public LoadResult loadData() {
		HomeProtocol hPro = new HomeProtocol();
		homeList = hPro.getEntity(0);
		
		pictures = hPro.getPictures();
		return check(homeList);
	}
	
	class HomeAdapter extends GlobalAdapter<AppEntity>{

		public HomeAdapter(ArrayList<AppEntity> list) {
			super(list);
		}

		@Override
		public GlobalHolder<AppEntity> getHolder(int position) {
			
			return new HomeHolder();
		}

		@Override
		public ArrayList<AppEntity> loadMore() {
			HomeProtocol pro = new HomeProtocol();
			ArrayList<AppEntity> moreList = pro.getEntity(getList().size());
			return moreList;
		}
		
	}
	static class ViewHolder{
		public TextView homeText;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AppEntity entity = (AppEntity) parent.getItemAtPosition(position);
		if (entity != null) {
			Intent intent = new Intent(getActivity(), AppDetailActivity.class);
			intent.putExtra("packageName", entity.getPackageName());
			startActivity(intent);
		}
		
	}
}
















