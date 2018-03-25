package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.rzm.commonlibrary.general.http.base.HttpUtils;
import com.rzm.commonlibrary.general.http.impl.callback.CallBackImpl;

public class TestHttpUtilsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_http_utils);
    }

    public void start(View view) {
        HttpUtils httpUtils2 = HttpUtils.with(this)
                .cache(true)
                .get()
                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .addParams("iid","6152551759")
                .addParams("aid","7")
                .execute(new CallBackImpl<String>() {

                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                        Toast.makeText(getApplicationContext(),"加载中。。。。。。。。。。",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(final Exception e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onSuccess(final String result) {
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }

                });
    }
}
