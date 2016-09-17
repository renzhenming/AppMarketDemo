package com.example.appmarket.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmarket.R;
import com.example.appmarket.entity.CategoryEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class NormalHolder extends GlobalHolder<CategoryEntity> implements
		OnClickListener {

	private ImageView icon1, icon2, icon3;
	private TextView name1, name2, name3;
	private LinearLayout grid1, grid2, grid3;
	private BitmapUtils mBitmapUtils;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.category_normal);
		icon1 = (ImageView) view.findViewById(R.id.iv_icon1);
		icon2 = (ImageView) view.findViewById(R.id.iv_icon2);
		icon3 = (ImageView) view.findViewById(R.id.iv_icon3);

		name1 = (TextView) view.findViewById(R.id.tv_name1);
		name2 = (TextView) view.findViewById(R.id.tv_name2);
		name3 = (TextView) view.findViewById(R.id.tv_name3);

		grid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
		grid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
		grid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);

		grid1.setOnClickListener(this);
		grid2.setOnClickListener(this);
		grid3.setOnClickListener(this);

		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshData(CategoryEntity data) {
		mBitmapUtils.display(icon1,
				HttpHelper.URL + "image?name=" + data.getUrl1());
		mBitmapUtils.display(icon2,
				HttpHelper.URL + "image?name=" + data.getUrl2());
		mBitmapUtils.display(icon3,
				HttpHelper.URL + "image?name=" + data.getUrl3());

		name1.setText(data.getName1());
		name2.setText(data.getName2());
		name3.setText(data.getName3());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_grid1:
			Toast.makeText(UIUtils.getContext(), name1.getText(), 0).show();
			break;
		case R.id.ll_grid2:
			Toast.makeText(UIUtils.getContext(), name2.getText(), 0).show();
			break;
		case R.id.ll_grid3:
			Toast.makeText(UIUtils.getContext(), name3.getText(), 0).show();
			break;

		default:
			break;
		}
	}

}
