package com.example.appmarket.holder;

import java.util.ArrayList;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class AppHolder extends GlobalHolder<AppEntity> {

	private BitmapUtils mBitmapUtils;
	private TextView mDes;
	private ImageView mDownload;
	private TextView mDownText;
	private ImageView mImage;
	private TextView mName;
	private RatingBar mRatingBar;
	private TextView mSize;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.item_app);
		// 创建加载图片的对象
		mBitmapUtils = BitmapHelper.getBitmapUtils();

		mDes = (TextView) view.findViewById(R.id.home_des);
		mDownload = (ImageView) view.findViewById(R.id.home_download);
		mDownText = (TextView) view.findViewById(R.id.home_download_text);
		mImage = (ImageView) view.findViewById(R.id.home_image);
		mName = (TextView) view.findViewById(R.id.home_name);
		mRatingBar = (RatingBar) view.findViewById(R.id.home_rating_bar);
		mSize = (TextView) view.findViewById(R.id.home_size);
		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		mDes.setText(data.getDes());
		mName.setText(data.getName());
		mRatingBar.setRating(data.getStars());
		mSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.getSize()));
		mBitmapUtils.display(mImage, HttpHelper.URL+"image?name="+data.getIconUrl());

	}

}
