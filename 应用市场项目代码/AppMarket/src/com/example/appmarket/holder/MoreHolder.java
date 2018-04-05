package com.example.appmarket.holder;

import com.example.appmarket.R;
import com.example.appmarket.utils.UIUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreHolder extends GlobalHolder<Integer> {

	// ���������ʾ��ǰ�Ƿ��м��ظ���Ĳ��ֺͼ��صĽ��
	public static final int TYPE_HAS_MORE = 0; // �м��ظ���
	public static final int TYPE_NO_MORE = 1; // û��
	public static final int TYPE_LOAD_ERROR = 2; // ����ʧ��
	private LinearLayout loadingView;
	private TextView loadErrorView;

	public MoreHolder(boolean hasMore) {
		setData(hasMore? TYPE_HAS_MORE : TYPE_NO_MORE);
	}

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.item_holder_more);
		loadingView = (LinearLayout) view.findViewById(R.id.loading);
		loadErrorView = (TextView) view.findViewById(R.id.load_error);
		return view;
	}
	
	
	@Override
	public void refreshData(Integer data) {
		switch (data) {
		case 0:
			loadingView.setVisibility(View.VISIBLE);
			loadErrorView.setVisibility(View.GONE);
			break;
		case 1:
			loadingView.setVisibility(View.GONE);
			loadErrorView.setVisibility(View.GONE);
			break;
		case 2:
			loadingView.setVisibility(View.GONE);
			loadErrorView.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

}
