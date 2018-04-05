package com.example.appmarket.holder;

import com.example.appmarket.R;
import com.example.appmarket.entity.CategoryEntity;
import com.example.appmarket.utils.UIUtils;

import android.view.View;
import android.widget.TextView;

public class TitleHolder extends GlobalHolder<CategoryEntity> {

	private TextView title;


	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.category_title);
		title = (TextView) view.findViewById(R.id.category_title);
		return view;
	}


	@Override
	public void refreshData(CategoryEntity data) {
		title.setText(data.getTitle());
	}

}
