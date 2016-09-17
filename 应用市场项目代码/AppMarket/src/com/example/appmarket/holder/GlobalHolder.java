package com.example.appmarket.holder;

import android.view.View;

public abstract class GlobalHolder<T> {

	private View convertView;
	private T data;

	// �ڹ��췽���н�����س�ʼ��
	public GlobalHolder() {
		// ����ÿһ����Ŀ���൱��convertview
		convertView = initView();
		// ����tag
		convertView.setTag(this);
	}

	// 1.��ʼ��item view ,ÿ��view��ͬ������ÿ������ʵ��
	public abstract View initView();

	// ����������ɣ�����������ݵĴ��?�õ���ݺ�ˢ����ʾ,ͬ���ǽ�������ȥ���
	public abstract void refreshData(T data);

	// �ṩgetter�������ڻ�ȡ��ݺ�view
	// ��������ṩһ������ˢ�����
	public void setData(T data) {
		this.data = data;
		refreshData(data);
	}

	public View getConvertView() {
		return convertView;
	}

	public void setConvertView(View convertView) {
		this.convertView = convertView;
	}

	public T getData() {
		return data;
	}

}
