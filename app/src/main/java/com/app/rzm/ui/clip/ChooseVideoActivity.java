package com.app.rzm.ui.clip;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mylibrary.view.indicator.recyclerview.view.WrapRecyclerView;
import com.app.rzm.R;
import com.rzm.commonlibrary.general.dialog.CommonDialog;
import com.rzm.commonlibrary.general.permission.PermissionDenied;
import com.rzm.commonlibrary.general.permission.PermissionHelper;
import com.rzm.commonlibrary.general.permission.PermissionSucceed;

import java.util.ArrayList;


/**
 * author rzm
 * function choose video
 * date 2017/12/5
 */
public class ChooseVideoActivity extends AppCompatActivity implements ChooseVideoAdapter.ChooseVideoListener {

    // 加载所有的数据
    private static final int LOAD_TYPE = 0x0021;

    WrapRecyclerView mImageList;
    LinearLayout mLoadingLayout;
    LinearLayout mEmptyLayout;

    private ArrayList<VideoInfo> mList = new ArrayList<>();
    private ChooseVideoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_video);
        initPermission();

    }

    private void initPermission() {
        PermissionHelper.with(this).requestCode(111).requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).request();
    }

    @PermissionSucceed(requestCode = 111)
    private void onGetPermission(){
        initButtonView();
        initData();
    }

    @PermissionDenied(requestCode = 111)
    private void onPermissionFiailed(){

    }


    private void initButtonView() {
        mImageList = (WrapRecyclerView) findViewById(R.id.image_list_rv);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mEmptyLayout = (LinearLayout) findViewById(R.id.empty_layout);

        mImageList.addLoadingView(mLoadingLayout);
        mImageList.addEmptyView(mEmptyLayout);
    }

    protected void initData() {

        mAdapter = new ChooseVideoAdapter(this, mList);
        mAdapter.setOnSelectImageListener(this);
        mImageList.setLayoutManager(new GridLayoutManager(this, 3));
        mImageList.addItemDecoration(new ThreeColumnItemDecoration(5));
        mImageList.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOAD_TYPE, null, mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.WIDTH,
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String select = VIDEO_PROJECTION[4]+"=? or "+VIDEO_PROJECTION[5]+"=?";
            String selectArgs[] = new String[]{"720","720"};
            CursorLoader cursorLoader = new CursorLoader(ChooseVideoActivity.this,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                    null,
                    null, VIDEO_PROJECTION[3] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.getCount() > 0) {
                ArrayList<VideoInfo> images = new ArrayList<>();
                while (data.moveToNext()) {

                    VideoInfo info = new VideoInfo();
                    info.path = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                    info.id = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
                    info.time = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
                    System.out.println("aaaa 高:"+data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4])));
                    System.out.println("aaaa 宽:"+data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[5])));
                    images.add(info);
                }
                mList.addAll(images);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }

    };

    @Override
    public void onImageSelect(View view, String path, int position) {
        ExtractVideoInfoUtil extract = new ExtractVideoInfoUtil(path);
        String videoLength = extract.getVideoLength();
        if (!TextUtils.isEmpty(videoLength)){
            long duration = Long.valueOf(videoLength);
            if (duration <= 15000){
                //不需要裁剪
                Toast.makeText(getApplicationContext(),"视频小于15是，不需要裁剪",Toast.LENGTH_SHORT).show();
            }else if (duration > 15000 && duration <=60000){
                //需要裁剪
                Intent activity = new Intent(getApplicationContext(),VideoEditActivity.class);
                activity.putExtra(VideoEditActivity.PATH,path);
                startActivity(activity);
            }else{
                //超过60s
                showVideoTooBigDialog();
            }
        }
    }

    private void showVideoTooBigDialog() {
        final CommonDialog dialog = new CommonDialog.Builder(this)
                .setContentView(R.layout.dialog_one_button)
                .widthPercent(0.9f)
                .setText(R.id.dialog_des, "您的视频已超过一分钟，请选择较小的短视频上传")
                .setText(R.id.dialo_confirm, "好的")
                .show();
        dialog.setOnClickListener(R.id.dialo_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
