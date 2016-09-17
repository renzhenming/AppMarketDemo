package com.example.appmarket.holder;

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

public class AppDetailHolder extends GlobalHolder<AppEntity> {

	private ImageView ivIcon;
	private TextView tvName;
	private TextView tvSize;
	private TextView tvDownloadNum;
	private TextView tvVersion;
	private TextView tvDate;
	private RatingBar rbStar;

	private BitmapUtils mBitmapUtils;
	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		tvName = (TextView) view.findViewById(R.id.tv_name);
		tvSize = (TextView) view.findViewById(R.id.tv_size);
		tvDownloadNum = (TextView) view.findViewById(R.id.tv_download_num);
		tvVersion = (TextView) view.findViewById(R.id.tv_version);
		tvDate = (TextView) view.findViewById(R.id.tv_date);
		rbStar = (RatingBar) view.findViewById(R.id.rb_star);

		mBitmapUtils = BitmapHelper.getBitmapUtils();

		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		mBitmapUtils.display(ivIcon, HttpHelper.URL+"image?name="+data.getIconUrl());

		tvName.setText(data.getName());
		tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.getSize()));
		tvDownloadNum.setText("下载量:" + data.getDownloadNum());
		tvVersion.setText("版本:" + data.getVersion());
		tvDate.setText(data.getDate());
		rbStar.setRating(data.getStars());
	}

}
