package com.example.renzhenming.appmarket.ui.selectimage;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.example.mylibrary.BaseSkinActivity;
import com.example.renzhenming.appmarket.R;

import java.util.ArrayList;

public class TestImageActivity extends BaseSkinActivity {
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
        // 6.0 请求权限，危险权限，读取内存卡，拍照
        //中间搞一层不让开发者关注太多
        /*Intent intent = new Intent(this,ChoosePictureActivity.class);
        intent.putExtra(ChoosePictureActivity.EXTRA_SELECT_COUNT,9);
        intent.putExtra(ChoosePictureActivity.EXTRA_SELECT_MODE,ChoosePictureActivity.MODE_MULTI);
        intent.putStringArrayListExtra(ChoosePictureActivity.EXTRA_DEFAULT_SELECTED_LIST, mImageList);
        intent.putExtra(ChoosePictureActivity.EXTRA_SHOW_CAMERA, true);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);*/

        // 第一个只关注想要什么，良好的封装性，不要暴露太多
        ImageSelector.create().count(9).multi().origin(mImageList)
                .showCamera(true).start(this, SELECT_IMAGE_REQUEST);
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
