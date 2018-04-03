package com.app.rzm.test;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.general.permission.PermissionDenied;
import com.rzm.commonlibrary.general.permission.PermissionHelper;
import com.rzm.commonlibrary.general.permission.PermissionPermanentDenied;
import com.rzm.commonlibrary.general.permission.PermissionSucceed;

public class TestPermissionHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_permission_helper);

        PermissionHelper.with(this).requestCode(111).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
        }).request();
    }

    @PermissionSucceed(requestCode = 111)
    public void onPermissionGranted(){
        Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();

    }

    @PermissionPermanentDenied(requestCode = 111)
    public void PermissionPermanentDenied(String permission){
        Toast.makeText(getApplicationContext(),"您永久拒绝了权限"+permission+"，请去设置页面开启",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 111)
    public void PermissionDenied(String permission){
        Toast.makeText(getApplicationContext(),"授权被拒绝:"+permission,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions,grantResults);
    }
}
