package com.example.appmarket.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class NormalListView extends ListView {

	public NormalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initListView();
	}

	public NormalListView(Context context, AttributeSet attrs) {
		this(context, attrs,-1);
	}

	public NormalListView(Context context) {
		this(context,null);
	}

	public void initListView(){
		setSelector(new ColorDrawable());
		setDivider(null);
		setCacheColorHint(Color.TRANSPARENT);
	}
	
}
