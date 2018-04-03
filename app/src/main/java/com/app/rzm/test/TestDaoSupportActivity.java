package com.app.rzm.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.rzm.R;
import com.app.rzm.bean.Person;
import com.rzm.commonlibrary.general.db.DaoSupportFactory;
import com.rzm.commonlibrary.general.db.IDaoSupport;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDaoSupportActivity extends AppCompatActivity {

    private static final String TAG = "TestDaoSupportActivity";
    private IDaoSupport<Person> dao;
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dao_support);

        //数据库
        dao = DaoSupportFactory.getFactory(this).getDao(Person.class);
        //面向对象的六大思想，最少的知识原则
        //dao.insert(new Person("rzm",26));

        //批量插入测试
        persons = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            persons.add(new Person("rzm",26+i));
        }
    }

    public void insert(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                dao.insert(persons);
                final long end = System.currentTimeMillis();
                LogUtils.d(TAG,"time ->"+(end - start));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"插入耗时："+(end - start),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void query(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Person> list = dao.querySupport().selection("age = ?").selectionArgs("33").query();
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e(TAG,"list ->"+list.get(i).getName()+","+list.get(i).getAge());
                }
                final List<Person> allList = dao.querySupport().queryAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"查询到"+allList.size()+"条数据"+33+"岁的人有"+list.size()+"个",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void update(View view) {
        int update = dao.update(new Person("zmr", 33), "age=?", new String[]{"33"});
        LogUtils.e(TAG,"delete ->>haha:"+update);

        if (update > 0)
            Toast.makeText(getApplicationContext(),"更新"+update+"条数据",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        int delete = dao.delete("age=?", new String[]{"33"});
        LogUtils.e(TAG,"delete ->>"+delete);
        if (delete > 0)
            Toast.makeText(getApplicationContext(),"删除"+delete+"条数据",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
    }

    public void deleteAll(View view) {
        int delete = dao.deleteAll();
        LogUtils.e(TAG,"delete ->>"+delete);
        if (delete > 0)
            Toast.makeText(getApplicationContext(),"删除"+delete+"条数据",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
    }
}
