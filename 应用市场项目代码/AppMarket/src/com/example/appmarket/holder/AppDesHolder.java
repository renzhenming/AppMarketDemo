package com.example.appmarket.holder;

import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppEntity;
import com.example.appmarket.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class AppDesHolder extends GlobalHolder<AppEntity> implements OnClickListener {

	private TextView tv_des;
	private TextView tv_author;
	private RelativeLayout rl_toggle;
	private ImageView iv_arrow;
	
	private int measuredHeight;
	private int sevenLineHeight;
	private LinearLayout.LayoutParams params;
	private boolean isOpen;
	@Override
	public View initView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.layout_detail_desinfo, null);
		tv_des = (TextView) view.findViewById(R.id.tv_detail_des);
		tv_author = (TextView) view.findViewById(R.id.tv_detail_author);
		rl_toggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
		rl_toggle.setOnClickListener(this);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		return view;
	}

	@Override
	public void refreshData(final AppEntity data) {
		
		tv_des.setText(data.getDes());
		
		tv_author.setText(data.getAuthor());
		iv_arrow.setBackgroundResource((R.drawable.arrow_down));
		//异步执行，保证在页面设置完成之后再测量高度
		UIUtils.getHandler().post(new Runnable() {


			@Override
			public void run() {
				//完整的高度				
				measuredHeight = tv_des.getMeasuredHeight();				
				//7行的高度				
				sevenLineHeight = getSevenLineHeight(data);
				params = (android.widget.LinearLayout.LayoutParams) tv_des.getLayoutParams();
				params.height = sevenLineHeight;
				tv_des.setLayoutParams(params);
				
			}
		});
		
		
		
	}
	/***
	 *  android:id="@+id/tv_detail_des"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#9e9e9e"
        android:textSize="14sp"
	 * @return
	 */
	//获取7行文字的高度
	public int getSevenLineHeight(AppEntity data){
//		tv_des.measure(0, 0);加上导致有些高度测量不准确
		int measuredWidth = tv_des.getMeasuredWidth();
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
		
		TextView view = new TextView(UIUtils.getContext());
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		view.setText(data.getDes());
		view.setMaxLines(7);
		view.measure(widthMeasureSpec, heightMeasureSpec);
		int measuredHeight = view.getMeasuredHeight();
		
		return measuredHeight;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_detail_toggle:
			toggle();
			break;

		default:
			break;
		}
	}

	private void toggle() {
		ValueAnimator animator = null;
		if (!isOpen) {
			if (sevenLineHeight < measuredHeight) {
				animator = ValueAnimator.ofInt(sevenLineHeight,measuredHeight);
				isOpen = true;
			}
			
		}else{
			if (sevenLineHeight < measuredHeight) {
				animator = ValueAnimator.ofInt(measuredHeight,sevenLineHeight);
				isOpen = false;
			}
			
		}
		if (animator != null) {
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
			animator.setDuration(300);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator anim) {
					Integer value = (Integer) anim.getAnimatedValue();
					params.height = value;
					tv_des.setLayoutParams(params);
					final ScrollView scrollView = getScrollView();
					scrollView.post(new Runnable() {
						
						@Override
						public void run() {
							scrollView.fullScroll(ScrollView.FOCUS_DOWN);
						}
					});
				}
			});
			animator.start();
		}
		
	}
	
	//获取父kongjainscrollview
	public ScrollView getScrollView(){
		ViewParent parent = tv_des.getParent();
		while (!(parent instanceof ScrollView)) {
			parent = parent.getParent();
		
		}
		
		return (ScrollView)parent;
	}

}
