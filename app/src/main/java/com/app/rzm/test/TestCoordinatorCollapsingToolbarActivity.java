package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.rzm.R;

public class TestCoordinatorCollapsingToolbarActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_coordinator_collapsing_toolbar);
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("作品");*/
    }
}
