package com.example.appmarket.holder;

import java.util.ArrayList;

import android.R.anim;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.entity.AppEntity.SafeBean;
import com.example.appmarket.http.HttpHelper;
import com.example.appmarket.utils.BitmapHelper;
import com.example.appmarket.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class AppSafeHolder extends GlobalHolder<AppEntity> implements OnClickListener {

	private ImageView[] iv_safes;
	private ImageView[] iv_des;
	private TextView[] tv_des;
	private LinearLayout[] ll_des;
	private BitmapUtils mBitmapUtils;
	private RelativeLayout rl_des_root;
	private LinearLayout layoutToAnimate;
	private int measuredHeight;
	private LinearLayout.LayoutParams params;
	private boolean isOpen;
	private ImageView iv_arrow;

	@Override
	public View initView() {
		View view = View.inflate(UIUtils.getContext(),
				R.layout.layout_detail_safeinfo, null);
		iv_safes = new ImageView[4];
		iv_safes[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		iv_safes[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		iv_safes[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		iv_safes[3] = (ImageView) view.findViewById(R.id.iv_safe4);

		iv_des = new ImageView[4];
		iv_des[0] = (ImageView) view.findViewById(R.id.iv_des1);
		iv_des[1] = (ImageView) view.findViewById(R.id.iv_des2);
		iv_des[2] = (ImageView) view.findViewById(R.id.iv_des3);
		iv_des[3] = (ImageView) view.findViewById(R.id.iv_des4);

		tv_des = new TextView[4];
		tv_des[0] = (TextView) view.findViewById(R.id.tv_des1);
		tv_des[1] = (TextView) view.findViewById(R.id.tv_des2);
		tv_des[2] = (TextView) view.findViewById(R.id.tv_des3);
		tv_des[3] = (TextView) view.findViewById(R.id.tv_des4);

		ll_des = new LinearLayout[4];
		ll_des[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		ll_des[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		ll_des[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		ll_des[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
		
		rl_des_root = (RelativeLayout) view.findViewById(R.id.rl_des_root);
		rl_des_root.setOnClickListener(this);
		//CHU始化高度为0
		layoutToAnimate = (LinearLayout) view.findViewById(R.id.ll_des_root);
		params = (android.widget.LinearLayout.LayoutParams) layoutToAnimate.getLayoutParams();
		params.height = 0;
		isOpen =false;
		layoutToAnimate.setLayoutParams(params);
		
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		mBitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshData(AppEntity data) {
		ArrayList<SafeBean> safe = data.getSafe();

		for (int i = 0; i < 4; i++) {
			if (i < safe.size()) {
				SafeBean safeBean = safe.get(i);
				mBitmapUtils.display(iv_safes[i], HttpHelper.URL+"image?name="+safeBean.getSafeUrl());
				mBitmapUtils.display(iv_des[i], HttpHelper.URL+"image?name="+safeBean.getSafeDesUrl());
				tv_des[i].setText(safeBean.getSafeDes());

			}else{
				//根据服务器返回的数据决定多余的控件隐藏
				iv_safes[i].setVisibility(View.GONE);
				ll_des[i].setVisibility(View.GONE);
			}
		}
		
		
		//获取高度,要在数据设置完成之后开始测量，因为并不是每一个高度都是相同的
		layoutToAnimate.measure(0, 0);
		measuredHeight = layoutToAnimate.getMeasuredHeight();
		System.out.println("measureHeight:"+measuredHeight);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_des_root:
			toggle();
			
			break;

		default:
			break;
		}
		
	}

	private void toggle() {
		ValueAnimator animator;
		if (!isOpen) {
			animator = ValueAnimator.ofInt(0,measuredHeight);
			isOpen = true;
		}else{
			animator = ValueAnimator.ofInt(measuredHeight,0);
			isOpen = false;
		}
		
		animator.setDuration(300);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator anim) {
				Integer value = (Integer) anim.getAnimatedValue();
				params.height = value;
				layoutToAnimate.setLayoutParams(params);
			}
		});
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				if (isOpen) {
					
					iv_arrow.setBackgroundResource(R.drawable.arrow_up);
				}else{
					iv_arrow.setBackgroundResource(R.drawable.arrow_down);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
		animator.start();
	}

}
