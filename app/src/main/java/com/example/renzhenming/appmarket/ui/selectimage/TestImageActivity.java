package com.example.renzhenming.appmarket.ui.selectimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mylibrary.BaseSkinActivity;
import com.example.renzhenming.appmarket.R;
import com.rzm.commonlibrary.general.permission.PermissionFailed;
import com.rzm.commonlibrary.general.permission.PermissionHelper;
import com.rzm.commonlibrary.general.permission.PermissionSucceed;

import java.util.ArrayList;

public class TestImageActivity extends BaseSkinActivity {
    private static final int READ_STORAGE = 123;
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

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test_image);
    }

    // 选择图片
    public void selectImage(View view){
        // 6.0 请求权限，读取内存卡，拍照
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA
        }).request();
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.RECORD_AUDIO
        }).request();
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.RECORD_AUDIO
        }).request();
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.ACCESS_FINE_LOCATION
        }).request();
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.READ_CONTACTS
        }).request();
        PermissionHelper.with(this).requestCode(READ_STORAGE).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_CONTACTS
        }).request();
    }

    @PermissionSucceed(requestCode = READ_STORAGE)
    public void onPermissionGranted(){
        ImageSelector.create().count(9).multi().origin(mImageList)
                    .showCamera(true).start(this, SELECT_IMAGE_REQUEST);
    }

    @PermissionFailed(requestCode = READ_STORAGE)
    public void onPermissionDenied(){
        Toast.makeText(getApplicationContext(),"用户不想再收到申请权限的提示了",Toast.LENGTH_SHORT).show();
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
            /*Bitmap bitmap = ImageUtil.decodeFile(path);
            // 调用写好的native方法
            // 用Bitmap.compress压缩1/10
            ImageUtil.compressBitmap(bitmap, 75,
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                            new File(path).getName()
            );*/
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
