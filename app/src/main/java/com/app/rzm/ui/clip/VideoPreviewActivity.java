package com.app.rzm.ui.clip;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rzm.R;
import com.rzm.commonlibrary.general.dialog.CommonDialog;
import com.rzm.commonlibrary.views.FullScreenVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPreviewActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    FullScreenVideoView mVideoView;
    @BindView(R.id.video_share)
    TextView mVideoShare;
    @BindView(R.id.video_finish)
    ImageView mVideoFinish;
    @BindView(R.id.video_play)
    ImageView mVideoPlay;

    public static final String PATH = "path";
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        ButterKnife.bind(this);
        initIntentData();
        initData();
    }

    private void initData() {

        mVideoView.setVideoPath(mPath);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoPlay.setVisibility(View.VISIBLE);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showErrorDialog();
                return true;
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
    }

    private void showErrorDialog() {
        TextView view = new TextView(this);
        view.setText("好的");
        final CommonDialog dialog = new CommonDialog.Builder(this)
                .setContentView(view)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null){
            mVideoPlay.setVisibility(View.GONE);
            mVideoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null){
            mVideoPlay.setVisibility(View.VISIBLE);
            mVideoView.pause();
        }
    }

    private void initIntentData() {
        mPath = getIntent().getStringExtra(PATH);
    }

    @OnClick({R.id.video_view, R.id.video_share, R.id.video_finish,R.id.video_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_view:
                break;
            case R.id.video_share:
                //NavigateUtils.navigateToNewFeed(this,1,mPath);
                break;
            case R.id.video_finish:
                finish();
            case R.id.video_play:
                if (!mVideoView.isPlaying()){
                    mVideoPlay.setVisibility(View.GONE);
                    mVideoView.start();
                }
                break;
        }
    }
}
