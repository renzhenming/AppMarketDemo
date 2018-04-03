package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.general.dialog.CommonDialog;

public class TestCommonDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_common_dialog);
    }


    public void show(View view) {
        final CommonDialog dialog = new CommonDialog.Builder(this)
                .setContentView(R.layout.test_dialog_common)
                .setText(R.id.dialog_des, "我是dialog")
                .setText(R.id.cancel, "取消")
                .setText(R.id.confirm, "确定")
                .setCancelable(true)
                .show();
        dialog.setOnClickListener(R.id.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"确定",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setOnClickListener(R.id.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"取消",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
