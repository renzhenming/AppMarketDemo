package com.example.renzhenming.appmarket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

public class TestJniActivity extends Activity {

    static {
        /*
        加载动态库，动态库加载的时候 JNI_OnLoad函数会被调用
        在JNI_OnLoad函数中，Java虚拟机通过函数表的形式将JNI函数和java类中native函数对应起来
         */
        System.loadLibrary("compress_image");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jni);
        TextView textView= (TextView) findViewById(R.id.text);
        textView.setText(compressBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.back),1,"adf"));
    }

    /*
      Jni 函数的声明
      当调用到此函数时，java虚拟机会通过JNI_OnLoad里注册的函数表找到对应的函数去执行
    */
    //private native static String compressBitmap();

    private native static String compressBitmap(Bitmap bitmap, int quality, String fileName);

}
