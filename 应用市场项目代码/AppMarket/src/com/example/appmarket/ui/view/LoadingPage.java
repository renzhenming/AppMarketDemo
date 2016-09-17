package com.example.appmarket.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.appmarket.R;
import com.example.appmarket.thread.ThreadPoolManager;
import com.example.appmarket.utils.UIUtils;

public abstract class LoadingPage extends FrameLayout {

	// ���弯��״̬����ʾ��ǰӦ��ʾ�ļ���״̬
	private static final int STATE_LOADING_NONE = 0; // δ����
	private static final int STATE_LOADING = 1; // ������
	private static final int STATE_LOADING_ERROR = 2;// ����ʧ��
	private static final int STATE_LOADING_EMPTY = 3;// ����Ϊ��
	private static final int STATE_LOADING_SUCCESS = 4;// ���سɹ�
	// ������������ʾ��ǰ����״̬��Ĭ��Ϊδ����
	/** 切不可定义为静态的，否则后果严重 */
	public int mCurrentState = STATE_LOADING_NONE;

	private View loadingView;
	private View loadingViewError;
	private View loadingEmptyView;
	private View loadingSuccessView;

	public LoadingPage(Context context) {
		super(context);
		initView();
	}

	@SuppressWarnings("unused")
	private void initView() {
		// ��������Ӽ����в���

		if (loadingView == null) {
			loadingView = onCreateLoadingView();
			addView(loadingView);
		}
		// ��Ӽ���ʧ�ܲ���
		if (loadingViewError == null) {
			loadingViewError = onCreateLoadingErrorView();
			addView(loadingViewError);
		}
		// ��Ӽ������Ϊ�յĲ���
		if (loadingEmptyView == null) {
			loadingEmptyView = onCreateLoadingEmptyView();
			addView(loadingEmptyView);
		}

		// ��ݵ�ǰ״̬��ʾ��ȷ�Ĳ���
		showRightLayout();
	}

	// ��ݵ�ǰ״̬��ʾ��ȷ�Ĳ���
	@SuppressWarnings("unused")
	private void showRightLayout() {
		if (loadingView != null) {
			loadingView
					.setVisibility((mCurrentState == STATE_LOADING || mCurrentState == STATE_LOADING_NONE) ? View.VISIBLE
							: View.GONE);

		}
		if (loadingEmptyView != null) {
			loadingEmptyView
					.setVisibility(mCurrentState == STATE_LOADING_EMPTY ? View.VISIBLE
							: View.GONE);
		}
		if (loadingViewError != null) {
			loadingViewError
					.setVisibility(mCurrentState == STATE_LOADING_ERROR ? View.VISIBLE
							: View.GONE);
		}

		// ��Ӽ��سɹ��Ĳ���,�����ӱ������showrightpage�����У���Ϊ���ڼ�����ݳɹ���ʱ�������������
		// �����ӳɹ����ֲ��������ô������־Ͳ��ᱻ��ӣ�����������ж�loadingsuccessviewΪ�գ���ȻҲ���޷���ʾ�ɹ���������
		if (loadingSuccessView == null
				&& mCurrentState == STATE_LOADING_SUCCESS) {
			loadingSuccessView = oncreateSuccessView();
			// ����ʵ�ּ��سɹ���ҳ����������ʵ�ֵģ�Ϊ�˷�ֹ���಻ʵ�ֶ��null����Ҫ��һ���ж�
			if (loadingSuccessView != null) {
				addView(loadingSuccessView);
			}

		}

		if (loadingSuccessView != null) {
			loadingSuccessView
					.setVisibility(mCurrentState == STATE_LOADING_SUCCESS ? View.VISIBLE
							: View.GONE);

		}
	}

	// ���������в���
	public View onCreateLoadingView() {
		View loadingView = UIUtils.inflate(R.layout.view_loading_view);
		return loadingView;
	}

	// ��������ʧ�ܲ���
	public View onCreateLoadingErrorView() {
		View loadingViewError = UIUtils.inflate(R.layout.view_loading_error);
		Button reLoad = (Button) loadingViewError.findViewById(R.id.reloadData);
		reLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onLoad();
			}
		});
		return loadingViewError;
	}

	// ��������Ϊ�ղ���
	public View onCreateLoadingEmptyView() {
		View loadingEmptyView = UIUtils.inflate(R.layout.view_loading_empty);
		return loadingEmptyView;
	}

	// �������سɹ��Ĳ���,ÿ��ҳ��ʵ�ֵĶ���һ�����Ծ���ʵ�ֽ�������
	public abstract View oncreateSuccessView();

	// �˷���������ʵ�������������
	public abstract LoadResult loadData();

	// ���ֲ��־������չʾ���Ǹ������������ݵķ��ؽ���ģ�����������Ҫһ������ȥ������£���������Ҫ�ǳ������ÿ��ҳ��ʵ��
	public void onLoad() {
		// �������������߳��н���
		ThreadPoolManager.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				// �õ���������Ľ��
				final LoadResult result = loadData();

				// ���½���������߳���ʵ��
				UIUtils.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (result != null) {
							mCurrentState = result.getState();
							showRightLayout();
						}

					}
				});
			}
		});
	};

	// ö��
	public enum LoadResult {
		RESULT_OK(STATE_LOADING_SUCCESS), RESULT_ERROR(STATE_LOADING_ERROR), RESULT_EMPTY(
				STATE_LOADING_EMPTY);

		private int state;

		private LoadResult(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}
	}

}
