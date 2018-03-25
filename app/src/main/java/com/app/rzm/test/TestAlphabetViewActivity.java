package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.views.AlphabetView;

public class TestAlphabetViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alphabet_view);
        AlphabetView alphabet = (AlphabetView) findViewById(R.id.alphabet);

        //设置当前选中的一直处于高亮状态
        alphabet.setClearAfterUp(false);
        alphabet.setOnAlphabetTouchListener(new AlphabetView.OnAlphabetTouchListener() {
            @Override
            public void onTouch(String letter, boolean isTouch) {
                Toast.makeText(getApplicationContext(),letter,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
