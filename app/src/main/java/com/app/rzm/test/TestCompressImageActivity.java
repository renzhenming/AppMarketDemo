package com.app.rzm.test;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.app.rzm.ui.selectimage.ChoosePictureActivity;
import com.app.rzm.utils.ImageUtil;
import com.rzm.commonlibrary.BaseSkinActivity;
import com.rzm.commonlibrary.general.navigationbar.impl.CommonNavigationBar;
import com.app.rzm.ui.selectimage.ImageSelector;
import com.rzm.commonlibrary.general.permission.PermissionDenied;
import com.rzm.commonlibrary.general.permission.PermissionHelper;
import com.rzm.commonlibrary.general.permission.PermissionPermanentDenied;
import com.rzm.commonlibrary.general.permission.PermissionSucceed;

import java.io.File;
import java.util.ArrayList;

public class TestCompressImageActivity extends BaseSkinActivity {
    private static final int READ_STORAGE = 123;
    private static final int CALL_PHONE = 124;
    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;


    @Override
    protected void initData() {


    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initTitle() {
        CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this)
                .setTitle("压缩图片")
                .setRightText("确定")
                .setRightTextColor(R.color.green)
                .setRightTextSize(14)
                .setBackgroundColor(R.color.gray)
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"确定",Toast.LENGTH_SHORT).show();
                    }
                })
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"退出",Toast.LENGTH_SHORT).show();
                    }
                })
                .setTitleTextColor(R.color.red)
                .setTitleTextSize(18)
                .build();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test_compress_image);
    }

    // 选择图片
    public void selectImage(View view){
        // 6.0 请求权限，读取内存卡，拍照
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
        }).request();
    }

    @PermissionSucceed(requestCode = READ_STORAGE)
    public void onPermissionGranted(){
        ImageSelector.create().count(9).multi().origin(mImageList)
                    .showCamera(true).start(this, SELECT_IMAGE_REQUEST);
    }

    @PermissionDenied(requestCode = READ_STORAGE)
    public void onPermissionDenied(){
        Toast.makeText(getApplicationContext(),"用户拒绝了一次权限，但是没有永久拒绝",Toast.LENGTH_SHORT).show();
    }

    @PermissionPermanentDenied(requestCode = READ_STORAGE)
    public void onPermissionPermanentDenied(){
        Toast.makeText(getApplicationContext(),"用户拒绝了一次权限，并且永久拒绝",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    public void compressImg(View view){


        // 把选择好的图片做了一下压缩
        for (String path : mImageList) {
            // 做优化  第一个decodeFile有可能会内存移除
            // 一般后台会规定尺寸  800  小米 规定了宽度 720
            // 上传的时候可能会多张 for循环 最好用线程池 （2-3）
            Bitmap bitmap = ImageUtil.decodeFile(path);
            // 调用写好的native方法
            // 用Bitmap.compress压缩1/10
            ImageUtil.compressBitmap(bitmap, 75,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                            new File(path).getName());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_IMAGE_REQUEST && data != null){
                mImageList = data.getStringArrayListExtra(ChoosePictureActivity.EXTRA_RESULT);
                // 做一下显示
                Log.e("TAG",mImageList.toString());
            }
        }
    }
}
