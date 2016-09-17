package com.example.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.SubjectEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class SubjectHolder extends GlobalHolder<SubjectEntity> {

	private ImageView image;
	private TextView des;
	private BitmapUtils mBitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.item_subject);
		image = (ImageView) view.findViewById(R.id.subject_image);
		des = (TextView) view.findViewById(R.id.subject_des);
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshData(SubjectEntity data) {
		mBitmapUtils.display(image, HttpHelper.URL+"image?name="+data.getUrl());
		des.setText(data.getDes());
	}

}
