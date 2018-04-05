package com.example.appmarket.adapter;

import java.util.ArrayList;

import com.example.appmarket.holder.GlobalHolder;
import com.example.appmarket.holder.MoreHolder;
import com.example.appmarket.thread.ThreadPoolManager;
import com.example.appmarket.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class GlobalAdapter<T> extends BaseAdapter {
	// �ܵ���ݼ���
	private ArrayList<T> list;

	// ������Ŀ������
	private static final int TYPE_MORE = 0;
	private static final int TYPE_NORMAL = 1;

	public GlobalAdapter(ArrayList<T> list) {
		this.list = list;
	}

	// 本方法用于获取当前集合
	public ArrayList<T> getList() {
		return list;
	}

	@Override
	public int getCount() {
		// ��Ӽ��ظ�����Ŀ����ʱ��count��������1

		return list.size() + 1;
	}

	// ������Ŀ��listview��Ҫʵ������������
	@Override
	public int getViewTypeCount() {
		// ��Ŀ������,�������Ŀ�������ӣ�������д�˷���
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// ��Ŀ������,���һ����Ŀ���Ǽ��ظ��
		if (position == getCount() - 1) {
			return TYPE_MORE;
		} else {
			return getNormalType(position);

		}
	}

	// ���˼��ظ�����Ŀ֮��������type�ˣ�������б仯������д������������ظ��type
	public int getNormalType(int position) {

		return TYPE_NORMAL;
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GlobalHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			// ���ﲻ��ֱ��new����Ϊholder����δʵ�ֵĳ��󷽷�������Щ��������Ҫ�����������ʵ�֣�������Ҫ�ṩһ��������
			// ���࣬������ȥ��ȡ�����holder����
			if (type == TYPE_MORE) {
				// ���ظ��
				holder = new MoreHolder(hasMore());
			} else {
				holder = getHolder(position);
			}

		} else {
			holder = (GlobalHolder) convertView.getTag();
		}
		// ���ظ�����Ŀ�ǲ���Ҫ������ݵģ������ж�һ��
		if (type != TYPE_MORE) {
			// ˵������ͨ��Ŀ����ô���������
			holder.setData(getItem(position));
		} else {
			// ˵���Ǽ��ظ�����Ŀ�������Ŀ����ʾ�����Ϳ�ʼ���ظ����ݣ���Ҫ���ظ�����ǰ�����и����ݣ�������Ҫ�ж�һ��
			// ��ǰ�Ƿ��и�����
			MoreHolder moreHolder = (MoreHolder) holder;
			if (moreHolder.getData() == MoreHolder.TYPE_HAS_MORE) {
				onLoadMore(moreHolder);
			}

		}

		return holder.getConvertView();
	}

	// ���������д����������涨��ǰҳ���Ƿ��м��ظ���item��Ĭ��Ϊ��
	public boolean hasMore() {
		return true;
	}

	public abstract GlobalHolder<T> getHolder(int position);

	// ����һ��������¼��ǰ�Ƿ��ڼ��ظ�����
	private boolean isMoreDataIsLoading = false;

	// ����������׶˵�ʱ�򣬿�ʼ���ظ��
	public void onLoadMore(final MoreHolder holder) {
		if (!isMoreDataIsLoading) {

			ThreadPoolManager.getThreadPool().execute(new Runnable() {

				@Override
				public void run() {
					// ��ʼ���ظ����ݣ���������Ϊtrue
					isMoreDataIsLoading = true;
					// ���ظ����Ҫ������ȥʵ�֣�ͬ����һ������ķ����ṩ��ȥ
					final ArrayList<T> moreData = loadMore();

					/** �����漰�����½���ķ������߳��н��� */
					UIUtils.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// �ж�arraylist���Ƿ������
							if (moreData == null) {
								// û�����˵������ʧ��
								holder.setData(MoreHolder.TYPE_LOAD_ERROR);
							} else {
								// ����ݣ��ж���ݵ������Ƿ�=20�����������ݷ��������Զ����ã�����������һҳ��ݣ�����û����һҳ
								if (moreData.size() < 20) {
									// ��ݼ������
									holder.setData(MoreHolder.TYPE_NO_MORE);
								} else {
									// ������һҳ���
									holder.setData(MoreHolder.TYPE_HAS_MORE);
								}
								// �����ص��ĸ�����ݼ��뵽������
								list.addAll(moreData);
								// ˢ����ݼ���
								notifyDataSetChanged();
							}
						}
					});
					// ������Ϻ��ٴ���Ϊfalse,�´η��ɽ����ٴμ���
					isMoreDataIsLoading = false;
				}
			});
		}

	}

	public abstract ArrayList<T> loadMore();

}
