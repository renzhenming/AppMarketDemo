package com.app.rzm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mylibrary.view.passportview.PassportKeyboard;
import com.example.mylibrary.view.passportview.PasswordEditText;

public class TestPasswordKeyboardActivity extends AppCompatActivity implements PassportKeyboard.CustomerKeyboardClickListener {

    private PassportKeyboard mCustomerKeyboard;
    private PasswordEditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_password_keyboard);
        mPasswordEditText = (PasswordEditText) findViewById(R.id.password_et);
        mCustomerKeyboard = (PassportKeyboard) findViewById(R.id.custom_key_board);
        // 设置监听
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this);

    }

    /** * 键盘数字点击监听回调方法 */
    @Override public void click(String number) {
        mPasswordEditText.addPassword(number);
    }
    /** * 键盘删除点击监听回调方法 */
    @Override public void delete() {
        mPasswordEditText.deleteLastPassword();
    }

}
