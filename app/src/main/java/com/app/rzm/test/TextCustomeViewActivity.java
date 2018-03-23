package com.app.rzm.test;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.app.rzm.R;
import com.rzm.mylibrary.view.ArcView;

public class TextCustomeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_custormer_view);
        final ArcView mArcView = (ArcView) findViewById(R.id.arcView);
        mArcView.setTotalProgress(1000);
        ValueAnimator animator = ValueAnimator.ofFloat(0,600);
        animator.setDuration(2000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentProgress = (float) animation.getAnimatedValue();
                mArcView.setCurrentProgress((int) currentProgress);
            }
        });
    }
}
