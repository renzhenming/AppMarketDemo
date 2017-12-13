package com.example.renzhenming.appmarket.ui.clip;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renzhenming.appmarket.R;
import com.rzm.commonlibrary.general.videoeditor.cliper.VideoSimpleClipper;
import com.rzm.commonlibrary.views.FullScreenVideoView;
import com.rzm.commonlibrary.views.RangeSeekBar;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * author rzm
 * function edit video
 * date 2017/12/6
 */
public class VideoEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VideoEditActivity";
    // 最小剪辑时间3s
    private static final long MIN_CUT_DURATION = 3000L;
    //视频最多剪切多长时间
    private static final long MAX_CUT_DURATION = 15000L;
    //seekBar的区域内一共有多少张图片
    private static final int MAX_COUNT_RANGE = 10;
    //距离边缘的长度
    private static final float DEFAULT_LEFT_RIGHT = 35;
    //视频裁剪长度（以微秒为单位）
    private static final long VIDEO_CUT_DURATION = 15*1000 * 1000L;

    public static final String PATH = "path";

    private LinearLayout mSeekBarLayout;
    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    private int mMaxWidth;

    private long mDuration;
    private RangeSeekBar mSeekBar;
    private FullScreenVideoView mVideoView;
    private RecyclerView mRecyclerView;
    private ImageView mPositionIcon;
    private VideoAdapter mVideoEditAdapter;
    private float mAverageMsPx;
    private float mAveragePxMs;
    private String OutPutFileDirPath;
    private ExtractThread mExtractFrameWorkThread;
    private String mPath;
    private long mLeftProgress, mRightProgress;
    private long mScrollPos = 0;
    private int mScaledTouchSlop;
    private int mLastScrollX;
    private boolean mIsSeeking;
    private FrameLayout mLayoutBottom;
    private TextView mLayoutBottomText;
    private TextView mVideoTailor;
    private boolean mIsForeground = true;

    private boolean mIsOverScaledTouchSlop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        initButtonView();
        initData();
        initEditVideo();
        initPlay();
    }
    private void initButtonView() {
        mSeekBarLayout = (LinearLayout) findViewById(R.id.id_seekBarLayout);
        mLayoutBottom = (FrameLayout) findViewById(R.id.layout_bottom);
        mLayoutBottomText = (TextView) findViewById(R.id.layout_bottom_text);
        mVideoTailor = (TextView) findViewById(R.id.video_tailor);
        mVideoView = (FullScreenVideoView) findViewById(R.id.uVideoView);
        mPositionIcon = (ImageView) findViewById(R.id.positionIcon);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_rv_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mVideoEditAdapter = new VideoAdapter(this,
                (PictureUtils.getDisplayWidth(this) - PictureUtils.dip2px(this, 70)) / 10);
        mRecyclerView.setAdapter(mVideoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mVideoTailor.setOnClickListener(this);
    }

    private void initData() {

        mPath = getIntent().getStringExtra(PATH);
        if (!new File(mPath).exists()) {
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(mPath);
        mDuration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());

        mMaxWidth = PictureUtils.getDisplayWidth(this) - PictureUtils.dip2px(this, DEFAULT_LEFT_RIGHT*2);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    }

    private void initEditVideo() {
        long startPosition = 0;
        long endPosition = mDuration;
        //有多少张缩略图
        int thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
        //所有的缩略图的宽度之和
        int rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(PictureUtils.dip2px(this, DEFAULT_LEFT_RIGHT), thumbnailsCount));

        mLeftProgress = 0;
        mRightProgress = (int) Math.min(15000L,((mMaxWidth*1f / rangeWidth)* mDuration));

        //设置初始状态下seek bar的左右值
        mSeekBar = new RangeSeekBar(this, 0L, mRightProgress);
        mSeekBar.setSelectedMinValue(0L);
        mSeekBar.setSelectedMaxValue(endPosition);

        //设置最小裁剪时间
        mSeekBar.setMin_cut_time(MIN_CUT_DURATION);
        mSeekBar.setNotifyWhileDragging(true);
        mSeekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        mSeekBarLayout.addView(mSeekBar);

        //1px表示的时间（以毫秒为单位）
        mAverageMsPx = mDuration * 1.0f / rangeWidth * 1.0f;

        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = mMaxWidth / MAX_COUNT_RANGE;
        int extractH = PictureUtils.dip2px(this, 55);
        mExtractFrameWorkThread = new ExtractThread(extractW, extractH, mUIHandler, mPath, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();
        //1ms表示的长度（以px为单位）
        mAveragePxMs = (mMaxWidth * 1.0f / (mRightProgress - mLeftProgress));

    }


    private void initPlay() {
        mVideoView.setVideoPath(mPath);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        if (!mIsSeeking) {
                            videoStart();
                        }
                    }
                });
            }
        });
        videoStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video_tailor:
                if (mIsForeground){
                    mLayoutBottomText.setVisibility(View.GONE);
                    mLayoutBottom.setVisibility(View.VISIBLE);
                    mVideoTailor.setText("确定");
                    mIsForeground = false;
                }else{
                    clipVideo();
                }
                break;
        }
    }

    private void clipVideo() {
        if (mPath == null)
            return;
        VideoSimpleClipper.clip(mPath, mLeftProgress * 1000, VIDEO_CUT_DURATION, new VideoSimpleClipper.OnVideoClipFinishListener() {
            @Override
            public void onFinish(boolean success, String originPath, String clippedPath) {
                Toast.makeText(getApplicationContext(),"成功："+clippedPath,Toast.LENGTH_SHORT).show();
            }
        });
    }


    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mIsSeeking = false;
            } else {
                mIsSeeking = true;
                if (mIsOverScaledTouchSlop && mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mIsSeeking = false;
            int scrollX = getScrollXDistance();
            //达不到滑动的距离
            if (Math.abs(mLastScrollX - scrollX) < mScaledTouchSlop) {
                mIsOverScaledTouchSlop = false;
                return;
            }
            mIsOverScaledTouchSlop = true;

            //初始状态,scrollX默认的时候有35dp的空白，所以初始状态下scrollX = -35dp(-105px)
            if (scrollX == -PictureUtils.dip2px(VideoEditActivity.this, DEFAULT_LEFT_RIGHT)) {
                mScrollPos = 0;
            } else {
                // why 在这里处理一下,因为onScrollStateChanged早于onScrolled回调
                if (mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
                mIsSeeking = true;

                //移动过程中的毫秒值(+上35dp是为了抵消初始状态下scrollX移动过的的-35dp)
                mScrollPos = (long) (mAverageMsPx * (PictureUtils.dip2px(VideoEditActivity.this, DEFAULT_LEFT_RIGHT) + scrollX));
                long selectedMinValue = mSeekBar.getSelectedMinValue();
                mLeftProgress = selectedMinValue + mScrollPos;
                long selectedMaxValue = mSeekBar.getSelectedMaxValue();
                mRightProgress = selectedMaxValue + mScrollPos;
                mVideoView.seekTo((int) mLeftProgress);
            }
            mLastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     *
     * @return int px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    private ValueAnimator animator;

    private void anim() {
        if (mPositionIcon.getVisibility() == View.GONE) {
            mPositionIcon.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPositionIcon.getLayoutParams();
        int start = (int) (PictureUtils.dip2px(this, 35) + (mLeftProgress - mScrollPos) * mAveragePxMs);
        int end = (int) (PictureUtils.dip2px(this, 35) + (mRightProgress - mScrollPos) * mAveragePxMs);
        animator = ValueAnimator
                .ofInt(start, end)
                .setDuration((mRightProgress - mScrollPos) - (mLeftProgress - mScrollPos));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                mPositionIcon.setLayoutParams(params);
            }
        });
        animator.start();
    }

    private final MainHandler mUIHandler = new MainHandler(this);

    private static class MainHandler extends Handler {
        private final WeakReference<VideoEditActivity> mActivity;

        MainHandler(VideoEditActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoEditActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractThread.MSG_SAVE_SUCCESS) {
                    if (activity.mVideoEditAdapter != null) {
                        VideoInfo info = (VideoInfo) msg.obj;
                        activity.mVideoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }

    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, boolean isMin, RangeSeekBar.Thumb pressedThumb) {
            mLeftProgress = minValue + mScrollPos;
            mRightProgress = maxValue + mScrollPos;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mIsSeeking = false;
                    videoPause();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mIsSeeking = true;
                    mVideoView.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
                            mLeftProgress : mRightProgress));
                    break;
                case MotionEvent.ACTION_UP:
                    mIsSeeking = false;
                    mVideoView.seekTo((int) mLeftProgress);
                    break;
                default:
                    break;
            }
        }
    };


    private void videoStart() {
        mVideoView.start();
        mPositionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
        handler.removeCallbacks(run);
        handler.post(run);
    }

    private void videoProgressUpdate() {
        long currentPosition = mVideoView.getCurrentPosition();
        if (currentPosition >= (mRightProgress)) {
            mVideoView.seekTo((int) mLeftProgress);
            mPositionIcon.clearAnimation();
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            anim();
        }
    }

    private void videoPause() {
        mIsSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(run);
        }
        if (mPositionIcon.getVisibility() == View.VISIBLE) {
            mPositionIcon.setVisibility(View.GONE);
        }
        mPositionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo((int) mLeftProgress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            videoPause();
        }
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            videoProgressUpdate();
            handler.postDelayed(run, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null) {
            animator.cancel();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (mExtractVideoInfoUtil != null) {
            mExtractVideoInfoUtil.release();
        }
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
    }
}
