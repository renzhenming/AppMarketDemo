package com.example.renzhenming.appmarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class TestObservableActivity extends AppCompatActivity implements Observer{

    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_observable);
        mtextView = (TextView) findViewById(R.id.value);
        TextObserverClass.getObserver().addObserver(this);
    }

    public void change(View view){
        String value = "更新数据";
        TextObserverClass.getObserver().changeInfo(value);
    }

    @Override
    public void update(Observable o, Object arg) {
        mtextView.setText((String)arg);
    }

}
