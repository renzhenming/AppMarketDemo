package com.app.rzm.test;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.app.rzm.utils.BsUpdateUtils;
import com.example.mylibrary.BaseSkinActivity;
import com.rzm.commonlibrary.utils.AppSignatureUtils;
import com.rzm.commonlibrary.utils.AppUtils;

import java.io.File;

public class TestBsPatchActivity extends BaseSkinActivity {

    private String patch_path = Environment.getExternalStorageDirectory().getAbsolutePath()
            +File.separator+"patch.patch";

    private String resultApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +File.separator+"release.apk";

    private String oldApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +File.separator+"old.apk";

    private String newApkPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +File.separator+"new.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bs_patch);
    }

    public void diff(View view) {
        if (!new File(oldApkPath).exists()) {
            Toast.makeText(getApplicationContext(),"旧版本包不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new File(newApkPath).exists()) {
            Toast.makeText(getApplicationContext(),"新版本包不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        BsUpdateUtils.diff(oldApkPath,newApkPath,patch_path);
        Toast.makeText(getApplicationContext(),"差分包生成成功",Toast.LENGTH_SHORT).show();
    }

    public void patch(View view) {
        //耗时操作，开线程
        //getPackageResourcePath 安装的apk的路径
        if (!new File(patch_path).exists()) {
            Toast.makeText(getApplicationContext(),"差分包不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        BsUpdateUtils.combine(getPackageResourcePath(),resultApkPath,patch_path);
        //校验签名
        try {
            if (AppSignatureUtils.signatureEquals(AppSignatureUtils.getSignature(this),AppSignatureUtils.getSignature(newApkPath))){
                Toast.makeText(getApplicationContext(),"签名校验成功",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"签名校验失败",Toast.LENGTH_LONG).show();
                return;
            }
            //安装apk,7.0以下版本
            // AppUtils.installApp(this,new File(resultApkPath));

            //兼容7.0
            AppUtils.installCompat7(this,new File(resultApkPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    }

}
