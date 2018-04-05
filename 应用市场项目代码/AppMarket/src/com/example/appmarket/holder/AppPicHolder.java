package com.example.appmarket.holder;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

public class AppPicHolder extends GlobalHolder<AppEntity> {

	private ImageView [] pics;
	private BitmapUtils mBitmapUtils;
	@Override
	public View initView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.layout_detail_picinfo, null);
		pics = new ImageView[5];
		pics[0] = (ImageView) view.findViewById(R.id.iv_pic1);
		pics[1] = (ImageView) view.findViewById(R.id.iv_pic2);
		pics[2] = (ImageView) view.findViewById(R.id.iv_pic3);
		pics[3] = (ImageView) view.findViewById(R.id.iv_pic4);
		pics[4] = (ImageView) view.findViewById(R.id.iv_pic5);
		
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		ArrayList<String> screen = data.getScreen();
		for (int i = 0; i < 5 ; i++) {
			if (i < screen.size()) {
				
				mBitmapUtils.display(pics[i], HttpHelper.URL+"image?name="+screen.get(i));
			}else {
				pics[i].setVisibility(View.GONE);
			}
		}
	}
	
	
}
