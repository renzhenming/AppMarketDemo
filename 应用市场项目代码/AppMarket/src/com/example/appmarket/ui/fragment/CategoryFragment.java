package com.example.appmarket.ui.fragment;

import java.text.Normalizer;
import java.util.ArrayList;

import com.example.appmarket.adapter.GlobalAdapter;
import com.example.appmarket.entity.CategoryEntity;
import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.holder.NormalHolder;
import com.example.appmarket.holder.TitleHolder;
import com.example.appmarket.http.protocol.CategoryProtocol;
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

public class CategoryFragment extends BaseFragment {
	

	private ArrayList<CategoryEntity> list;
	@Override
	public View oncreateSuccessView() {
		NormalListView listview = new NormalListView(UIUtils.getContext());
		listview.setAdapter(new CategoryAdapter(list));
		return listview;
	}

	@Override
	public LoadResult loadData() {
		CategoryProtocol pro = new CategoryProtocol();
		list = pro.getEntity(0);
	for (int i = 0; i < list.size(); i++) {
		CategoryEntity entity = list.get(i);
		System.out.println("entity:"+entity);
	}
		return check(list);
	}
	class CategoryAdapter extends GlobalAdapter<CategoryEntity>{

		public CategoryAdapter(ArrayList<CategoryEntity> list) {
			super(list);
		}

		@Override
		public GlobalHolder<CategoryEntity> getHolder(int position) {
			CategoryEntity entity = list.get(position);
			if (entity.isTitle() ) {
				return new TitleHolder();
			}else{
				return new NormalHolder();
			}
		}
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		@Override
		public int getNormalType(int position) {
			CategoryEntity entity = list.get(position);
			if (entity.isTitle()) {
				System.out.println("title:"+entity.isTitle());
				return super.getNormalType(position)+1;
			}else{
				System.out.println("title:"+entity.isTitle());
				return super.getNormalType(position);
			}
			
		}
		
		@Override
		public boolean hasMore() {
			return false;
		}

		@Override
		public ArrayList<CategoryEntity> loadMore() {
			return null;
		}
		
	}
}















