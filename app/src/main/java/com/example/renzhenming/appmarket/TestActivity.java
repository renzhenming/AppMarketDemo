package com.example.renzhenming.appmarket;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.BaseSkinActivity;
import com.example.mylibrary.db.DaoSupportFactory;
import com.example.mylibrary.db.IDaoSupport;
import com.example.mylibrary.http.HttpCallBack;
import com.example.mylibrary.http.OkHttpEngine;
import com.example.mylibrary.navigation.CommonNavigationBar;
import com.example.renzhenming.appmarket.bean.Person;
import com.example.renzhenming.appmarket.test.MessageService;
import com.rzm.commonlibrary.general.FixDexManager;
import com.rzm.commonlibrary.general.dialog.CommonDialog;
import com.rzm.commonlibrary.general.http.HttpUtils;
import com.rzm.commonlibrary.general.navigationbar.StatusBarManager;
import com.rzm.commonlibrary.inject.BindViewId;
import com.rzm.commonlibrary.inject.ViewBind;
import com.rzm.commonlibrary.utils.LogUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseSkinActivity {

    private static final String TAG = "TestActivity";
    @BindViewId(R.id.click)
    TextView mText;
    private ImageView mImage;

    @Override
    protected void initData() {
        //路径url参数都需要放到jni中，防止反编译被盗取到url
        //（https无法被抓包，http可以）
        HttpUtils httpUtils = HttpUtils.with(this)
                .exchangeEngine(new OkHttpEngine())
                .cache(true)
                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .addParams("iid","6152551759")
                .addParams("aid","7")
                .execute(new HttpCallBack<String>() {

                    @Override
                    protected void onPreExecute() {

                    }

                    @Override
                    public void onError(final Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    @Override
                    public void onSuccess(final String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });



        //数据库
        final IDaoSupport<Person> dao = DaoSupportFactory.getFactory().getDao(Person.class);
        //面向对象的六大思想，最少的知识原则
        //dao.insert(new Person("rzm",26));

        //批量插入测试
        final List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            persons.add(new Person("rzm",26+i));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                dao.insert(persons);
                long end = System.currentTimeMillis();
                LogUtils.d(TAG,"time ->"+(end - start));
                //List<Person> query = dao.query();
                List<Person> list = dao.querySupport().selection("age = ?").selectionArgs("33").query();
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e(TAG,"list ->"+list.get(i).getName()+","+list.get(i).getAge());
                }

                int delete = dao.delete("age=?", new String[]{"28"});
                LogUtils.e(TAG,"delete ->>"+delete);
                int haha = dao.update(new Person("haha", 28), "age=?", new String[]{"27"});
                LogUtils.e(TAG,"delete ->>haha:"+haha);
            }
        }).start();

        ViewBind.inject(this);
        mText.setText("注入的值");


        /**
         * 在这里修复遇到一个问题，就是第一次启动无法达到修复的效果，只有再次启动才可以，我想大概
         * 是调用的时机不对，于是把这行代码放在了application中，果然修复成功，目前这个问题不确定是不是
         * 机型的问题，因为别人好像在activity中调用修复就没问题
         */
        // fixDex();
    }

    @Override
    protected void initView() {
        startService(new Intent(getApplicationContext(),MessageService.class));
        mImage = (ImageView) findViewById(R.id.image);
        new StatusBarManager.builder(this)
                .setTintType(StatusBarManager.TintType.PURECOLOR)
                .setStatusBarColor(R.color.colorPrimary)
                .build();
        CommonNavigationBar navigationBar = new CommonNavigationBar.Builder(this)
                .setToolbarEnable(true)
                .setTitle("个人中心")
                .setRightIcon(R.drawable.search)
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"编辑",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog dialog = new CommonDialog.Builder(TestActivity.this)
                        .setContentView(R.layout.dialog)

                        .setText(R.id.toast,"我是新的dialog")
                        .fullWidth()
                        .alignBottom(true)
                        .show();
                //我要获取到输入框的值，可以这样做 getView  (ListView RecyclerView CheckBox)
                /*final EditText mEditText = dialog.getView(输入框的id);
                dialog.setOnClickListener(R.id.toast, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test);
    }

    public void change(View view){
        try {
            //点击从手机中一个apk中获取图片资源并且设置给ImageView显示
            //获取系统的两个参数
            Resources superResources = getResources();
            //创建assetManger(无法直接new因为被hide了，所以用反射)
            AssetManager assetManager = AssetManager.class.newInstance();
            //添加资源目录（addAssetPath也是一样被hide无法直接调用）
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);//如果是私有的，添上防止万一某一天他变成了私有的
            method.invoke(assetManager,Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"red.skin");//注意你资源的名字要一致
            Resources resources = new Resources(assetManager,superResources.getDisplayMetrics(),superResources.getConfiguration());
            //用创建好的Resources获取资源(注意着三个参数，第一个是要获取资源的名字，我们设置的是girl，不要忘了，第二个参数代表这个资源在哪个文件夹中，第三个参数表示要获取资源的apk的包名，缺一不可)
            int identifier = resources.getIdentifier("girl", "drawable", "com.example.myapplication");
            if (identifier  != 0){
                Drawable drawable = resources.getDrawable(identifier);
                mImage.setImageDrawable(drawable);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void fixDex() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"fix.dex");
        if (file.exists()){
            FixDexManager manager = new FixDexManager(this);
            try {
                manager.fixDex(file.getAbsolutePath());
                Toast.makeText(getApplicationContext(),"修复bug成功",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"修复bug失败",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }



}
