package com.example.appmarket.holder;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.thread.DownloadEntity;
import com.example.appmarket.thread.DownloadManager;
import com.example.appmarket.thread.DownloadManager.DownloadObserver;
import com.example.appmarket.ui.view.ProgressHorizontal;
import com.example.appmarket.utils.UIUtils;

public class AppDownloadHolder extends GlobalHolder<AppEntity> implements
		DownloadObserver, OnClickListener {

	private Button shoucang;
	private Button download;
	private Button share;
	private FrameLayout flDownload;
	private DownloadManager manager;

	private float mProgress;
	private int mState;
	private ProgressHorizontal phProgress;

	@Override
	public View initView() {
		View view = View.inflate(UIUtils.getContext(),
				R.layout.layout_detail_download, null);
		shoucang = (Button) view.findViewById(R.id.btn_fav);
		download = (Button) view.findViewById(R.id.btn_download);
		share = (Button) view.findViewById(R.id.btn_share);
		flDownload = (FrameLayout) view.findViewById(R.id.fl_download);

		download.setOnClickListener(this);
		flDownload.setOnClickListener(this);

		phProgress = new ProgressHorizontal(UIUtils.getContext());
		phProgress.setProgressResource(R.drawable.progress_normal);
		phProgress.setProgressBackgroundResource(R.drawable.progress_bg);
		phProgress.setProgressTextColor(Color.WHITE);
		phProgress.setProgressTextSize(UIUtils.dp2px(17));
		flDownload.addView(phProgress);

		manager = DownloadManager.getDownloadManager();
		manager.registerDownloadObserver(this);
		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		if (data != null) {
			DownloadEntity entity = manager.getDownloadEntity(data.getId());
			if (entity != null) {
				// 不是第一次下载
				mProgress = entity.getCurrentProgress();
				mState = entity.getCurrentState();
			} else {
				// 是第一次下载
				mProgress = 0;
				mState = DownloadManager.STATE_NONE;
			}
			refreshUI(mProgress, mState);
		}

	}

	public void refreshUIOMainThread(final DownloadEntity entity) {
		if (entity.getId() == getData().getId()) {
			UIUtils.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					refreshUI(entity.getCurrentProgress(),entity.getCurrentState());
				}
			});
		}
		
	}

	// 根据进度和状态刷新
	public void refreshUI(float f,int state) {
		mProgress = f;
		mState = state;

		switch (mState) {
		case DownloadManager.STATE_NONE:
			// 未下载
			download.setVisibility(View.VISIBLE);
			flDownload.setVisibility(View.GONE);// 隐藏进度条
			download.setText("下载");
			break;
		case DownloadManager.STATE_WAITING:
			// 等待下载
			download.setVisibility(View.VISIBLE);
			flDownload.setVisibility(View.GONE);// 隐藏进度条
			download.setText("等待下载...");
			break;
		case DownloadManager.STATE_DOWNLOAD:
			// 正在下载
			download.setVisibility(View.GONE);
			flDownload.setVisibility(View.VISIBLE);// 显示进度条
			phProgress.setProgress(mProgress);// 更新进度
			phProgress.setCenterText("");// 去掉中心文字
			break;
		case DownloadManager.STATE_PAUSE:
			// 暂停下载
			download.setVisibility(View.GONE);
			flDownload.setVisibility(View.VISIBLE);// 显示进度条
			phProgress.setProgress(mProgress);// 更新进度
			phProgress.setCenterText("暂停");// 设置中心文字
			break;
		case DownloadManager.STATE_ERROR:
			// 下载失败
			download.setVisibility(View.VISIBLE);
			flDownload.setVisibility(View.GONE);// 隐藏进度条
			download.setText("下载失败");
			break;
		case DownloadManager.STATE_SUCCESS:
			// 下载成功
			download.setVisibility(View.VISIBLE);
			flDownload.setVisibility(View.GONE);// 隐藏进度条
			download.setText("安装");
			break;

		default:
			break;
		}
	}

	@Override
	public void onDownloadStateChanged(DownloadEntity downloadEntity) {
	
			refreshUIOMainThread(downloadEntity);

	}

	@Override
	public void onDownloadProgressChanged(DownloadEntity downloadEntity) {
			refreshUIOMainThread(downloadEntity);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
		case R.id.fl_download:
			if (mState == DownloadManager.STATE_NONE
					|| mState == DownloadManager.STATE_PAUSE
					|| mState == DownloadManager.STATE_ERROR) {
				manager.download(getData());
			} else if (mState == DownloadManager.STATE_DOWNLOAD
					|| mState == DownloadManager.STATE_WAITING) {
				manager.pause(getData());
			} else if (mState == DownloadManager.STATE_SUCCESS) {
				manager.install(getData());
			}
			break;

		default:
			break;
		}
	}

}
