package com.app.rzm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mylibrary.view.passportview.PassportKeyboard;
import com.example.mylibrary.view.passportview.PasswordEditText;
import com.rzm.commonlibrary.utils.ToastUtil;

public class TestPasswordKeyboardActivity extends AppCompatActivity implements PasswordEditText.OnInputFinishListener {

    private PassportKeyboard mCustomerKeyboard;
    private PasswordEditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_password_keyboard);
        mPasswordEditText = (PasswordEditText) findViewById(R.id.password_et);
        mCustomerKeyboard = (PassportKeyboard) findViewById(R.id.custom_key_board);
        mCustomerKeyboard.bindPasswordEditText(mPasswordEditText);

        mPasswordEditText.setOnInputFinishListener(this);
    }


    @Override
    public void onInputFinish(String number) {
        ToastUtil.showToast(this,"输入完成:"+number);
    }
}
