package com.example.appmarket.ui.activity;

import com.example.appmarket.R;
import com.example.appmarket.R.id;
import com.example.appmarket.R.layout;
import com.example.appmarket.R.menu;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.holder.AppDesHolder;
import com.example.appmarket.holder.AppDetailHolder;
import com.example.appmarket.holder.AppDownloadHolder;
import com.example.appmarket.holder.AppPicHolder;
import com.example.appmarket.holder.AppSafeHolder;
import com.example.appmarket.http.protocol.AppDetailProtocol;
import com.example.appmarket.ui.view.LoadingPage;
import com.example.appmarket.ui.view.LoadingPage.LoadResult;
import com.example.appmarket.utils.UIUtils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class AppDetailActivity extends BaseActivity {

	private LoadingPage mLoadingPage;
	private String mPackageName;

	private AppEntity entity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPackageName = getIntent().getStringExtra("packageName");
		
		mLoadingPage = new LoadingPage(UIUtils.getContext()) {
			

			@Override
			public View oncreateSuccessView() {
				return initView();
			}
			
			@Override
			public LoadResult loadData() {
				AppDetailProtocol protocol = new AppDetailProtocol(mPackageName);
				entity = protocol.getEntity(0);
				if (entity != null) {
					return LoadResult.RESULT_OK;
				}else{
					return LoadResult.RESULT_ERROR;
				}
			}
		};
		
		
		setContentView(mLoadingPage);
		mLoadingPage.onLoad();
		initActionBar();
	}
	
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public View initView(){
		View view = UIUtils.inflate(R.layout.layout_app_detail);

		//获取到顶部应用信息的布局
		FrameLayout appInfoContainer = (FrameLayout) view.findViewById(R.id.app_detail_appinfo);
		AppDetailHolder holder = new AppDetailHolder();
		appInfoContainer.addView(holder.getConvertView());
		holder.setData(entity);
		
		//获取应用安全信息的布局
		FrameLayout appSafeContainer = (FrameLayout) view.findViewById(R.id.app_safe_appinfo);
		AppSafeHolder safeHolder = new AppSafeHolder();
		appSafeContainer.addView(safeHolder.getConvertView());
		safeHolder.setData(entity);
		
		//获取应用截图的布局
		HorizontalScrollView appPicContainer = (HorizontalScrollView) view.findViewById(R.id.app_pic_appinfo);
		AppPicHolder picHolder = new AppPicHolder();
		appPicContainer.addView(picHolder.getConvertView());
		picHolder.setData(entity);
		
		//获取应用描述的布局
		FrameLayout appDesContainer = (FrameLayout) view.findViewById(R.id.app_des_appinfo);
		AppDesHolder desHolder = new AppDesHolder();
		appDesContainer.addView(desHolder.getConvertView());
		desHolder.setData(entity);
		
		//获取下载布局
		FrameLayout appDownloadContainer = (FrameLayout) view.findViewById(R.id.app_download_appinfo);
		AppDownloadHolder downloadHolder = new  AppDownloadHolder();
		downloadHolder.setData(entity);
		appDownloadContainer.addView(downloadHolder.getConvertView());
		return view;
	}

}
