package com.example.renzhenming.appmarket.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.mylibrary.BaseSkinActivity;
import com.example.mylibrary.navigation.CommonNavigationBar;
import com.example.renzhenming.appmarket.R;
import com.example.renzhenming.appmarket.adapter.ChoosePictureAdapter;
import com.example.renzhenming.appmarket.adapter.ChoosePictureListener;
import com.rzm.commonlibrary.inject.BindViewId;

import java.util.ArrayList;

/**
 * 1.可以单选或多选
 * 2.可以设置拍照按钮的显示
 * 3.再次进入显示之前选择的图片
 */
public class ChoosePictureActivity extends BaseSkinActivity implements ChoosePictureListener {

    // 带过来的Key
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    // 加载所有的数据
    private static final int LOAD_TYPE = 0x0021;
    /*****************
     * 获取传递过来的参数
     *****************/
    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;
    // 选择图片的模式 - 单选
    public static int MODE_SINGLE = 0x0012;
    // 单选或者多选，int类型的type
    private int mMode = MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;
    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;

    @BindViewId(R.id.image_list_rv)
    private RecyclerView mImageListRv;
    @BindViewId(R.id.select_num)
    private TextView mSelectNumTv;
    @BindViewId(R.id.select_preview)
    private TextView mSelectPreview;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }
        initImageList();

        exchangeViewShow();
    }

    private void exchangeViewShow() {

    }
    /**
     * 2.ContentProvider获取内存卡中所有的图片
     */
    private void initImageList() {
        // int id 查询全部
        getLoaderManager().initLoader(LOAD_TYPE,null,mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(ChoosePictureActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 解析，封装到集合  只保存String路径
            if (data != null && data.getCount() > 0) {
                ArrayList<String> images = new ArrayList<>();

                // 如果需要显示拍照，就在第一个位置上加一个空String
                if(mShowCamera){
                    images.add("");
                }
                // 不断的遍历循环
                while (data.moveToNext()) {
                    // 只保存路径
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    images.add(path);
                }

                // 显示列表数据
                showImageList(images);
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    /**
     * 3.展示获取到的图片显示到列表
     * @param images
     */
    private void showImageList(ArrayList<String> images) {
        ChoosePictureAdapter listAdapter = new ChoosePictureAdapter(this,images,mResultList,mMaxCount);
        listAdapter.setOnSelectImageListener(this);
        mImageListRv.setLayoutManager(new GridLayoutManager(this,4));
        mImageListRv.setAdapter(listAdapter);
    }


    @Override
    protected void initTitle() {
        CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this).setTitle("选择图片").build();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_choose_picture);
    }

    @Override
    public void select() {

    }
}
