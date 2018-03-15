package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.rzm.R;

import java.util.Observable;
import java.util.Observer;

public class TestObservableActivity extends AppCompatActivity implements Observer{

    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_observable);
        mtextView = (TextView) findViewById(R.id.value);
        TextObserverClass.getInstance().addObserver(this);
    }

    public void change(View view){
        String value = "更新数据";
        TextObserverClass.getInstance().changeInfo(value);
    }

    @Override
    public void update(Observable o, Object arg) {
        mtextView.setText((String)arg);
    }

}
