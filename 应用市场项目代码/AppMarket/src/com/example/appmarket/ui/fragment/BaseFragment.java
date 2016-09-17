package com.example.appmarket.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.ui.view.LoadingPage;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment<T> extends Fragment {
	
	private LoadingPage loadingPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadingPage = new LoadingPage(UIUtils.getContext()){

			@Override
			public View oncreateSuccessView() {
				//����basefragmentҲ�޷�����ÿ��ҳ����سɹ��Ĳ��֣����Ի���Ҫ�����������ʵ�֣�������Ҫһ�����󷽷�
				
				return BaseFragment.this.oncreateSuccessView();
			}

			@Override
			public LoadResult loadData() {
				//ͬ���ز�����ͬ������Ҳ��Ҫһ��������ݵĳ��󷽷�������ʵ��
				return  BaseFragment.this.loadData();
			}
			
		};
		
		return loadingPage;
	}
	//����һ�����󷽷�������ʵ�ִ���������ݳɹ��Ĳ���
	public abstract View oncreateSuccessView();
	public abstract LoadResult loadData();
	//����������ݵķ����ṩ������������ں��ʵ�ʱ��ȥ����
	public void onLoad(){
		if (loadingPage != null) {
			loadingPage.onLoad();
		}
	}

	public LoadResult check(Object data) {
		if (data != null) {
			if (data instanceof List) {
				List list = (List) data;
				if (list.isEmpty()) {
					return LoadResult.RESULT_EMPTY;
				}else{
					return LoadResult.RESULT_OK;
				}
			}
			
		}
		return LoadResult.RESULT_ERROR;
		
	}
}
