package com.example.appmarket.holder;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.thread.DownloadEntity;
import com.example.appmarket.thread.DownloadManager;
import com.example.appmarket.thread.DownloadManager.DownloadObserver;
import com.example.appmarket.ui.view.ProgressArc;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends GlobalHolder<AppEntity> implements DownloadObserver,OnClickListener {

	private TextView textview;
	private TextView mDes, mDownText, mName, mSize;
	FrameLayout mDownload;
	private ImageView mImage;
	private RatingBar mRatingBar;
	private BitmapUtils mBitmapUtils;
	private LinearLayout mDownloadContainer;
	private DownloadManager manager;
	private float mProgress;
	private int mState;
	private ProgressArc pa;

	@Override
	public View initView() {
		// 创建加载图片的对象
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		View view = UIUtils.inflate(R.layout.item_home);
		mDes = (TextView) view.findViewById(R.id.home_des);
		mDownload = (FrameLayout) view.findViewById(R.id.home_download);
		mDownloadContainer = (LinearLayout) view
				.findViewById(R.id.home_download_container);
		mDownloadContainer.setOnClickListener(this);
		
		pa = new ProgressArc(UIUtils.getContext());
		pa.setArcDiameter(UIUtils.dp2px(26));
		pa.setProgressColor(Color.parseColor("#0000ff"));

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				UIUtils.dp2px(27),
				UIUtils.dp2px(27));
		mDownloadContainer.addView(pa, params);
		
		manager = DownloadManager.getDownloadManager();
		manager.registerDownloadObserver(this);
		
		mDownText = (TextView) view.findViewById(R.id.home_download_text);
		mImage = (ImageView) view.findViewById(R.id.home_image);
		mName = (TextView) view.findViewById(R.id.home_name);
		mRatingBar = (RatingBar) view.findViewById(R.id.home_rating_bar);
		mSize = (TextView) view.findViewById(R.id.home_size);
		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		
		
		if (data != null) {
			mDes.setText(data.getDes());
			mName.setText(data.getName());
			mRatingBar.setRating(data.getStars());
			mSize.setText(Formatter.formatFileSize(UIUtils.getContext(),
					data.getSize()));
			mBitmapUtils.display(mImage,
					HttpHelper.URL + "image?name=" + data.getIconUrl());
			
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
			refreshUI(mProgress, mState,getData().getId());
		}

	}
	//在主线程更新UI
		private void refreshOnMainThread(final DownloadEntity info) {
			if (info.getId() == getData().getId()) {//当多个app同时下载时, 导致此方法产生多次回调, 此时需要过滤到和本app无关的数据
				UIUtils.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						refreshUI(info.getCurrentProgress(), info.getCurrentState(), info.getId());
					}
				});
			}
		}
	// 根据进度和状态刷新
		public void refreshUI(float progress,int state,int id) {
			//确保刷新的是正确的app(由于listview重用机制,导致后面的item重用了前面item的布局对象, 那么前面item的状态就会更新在当前item上,从而布局错乱)
			if (getData().getId() != id) {
				return;
			}
			mProgress = progress;
			mState = state;

			switch (mState) {
			case DownloadManager.STATE_NONE:
				// 未下载
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
				pa.setBackgroundResource(R.drawable.ic_download);
				mDownText.setText("下载");
				break;
			case DownloadManager.STATE_WAITING:
				// 等待下载
				pa.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
				pa.setBackgroundResource(R.drawable.ic_download);
				pa.setProgress(progress, true);
				mDownText.setText("等待");
				break;
			case DownloadManager.STATE_DOWNLOAD:
				// 正在下载
				pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);//无进度样式
				pa.setBackgroundResource(R.drawable.ic_pause);//更新图片
				pa.setProgress(progress, true);//参2:进度变化的动画

				int percent = (int) (progress * 100);
				mDownText.setText(percent + "%");
				break;
			case DownloadManager.STATE_PAUSE:
				// 暂停下载
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//无进度样式
				pa.setBackgroundResource(R.drawable.ic_resume);//更新图片

				mDownText.setText((int) (progress * 100) + "%");
				break;
			case DownloadManager.STATE_ERROR:
				// 下载失败
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//无进度样式
				pa.setBackgroundResource(R.drawable.ic_redownload);//更新图片

				mDownText.setText("失败");
				break;
			case DownloadManager.STATE_SUCCESS:
				// 下载成功
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//无进度样式
				pa.setBackgroundResource(R.drawable.ic_install);//更新图片

				mDownText.setText("安装");
				break;

			default:
				break;
			}
		}

	@Override
	public void onDownloadStateChanged(DownloadEntity downloadEntity) {
		refreshOnMainThread(downloadEntity);
	}

	@Override
	public void onDownloadProgressChanged(DownloadEntity downloadEntity) {
		refreshOnMainThread(downloadEntity);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_download_container:
		
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
