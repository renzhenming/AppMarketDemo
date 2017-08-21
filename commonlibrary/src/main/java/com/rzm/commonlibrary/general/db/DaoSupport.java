package com.rzm.commonlibrary.general.db;

import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;

/**
 * Created by renzhenming on 2017/8/22.
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    private Class<T> mClazz;
    private SQLiteDatabase mSqliteDatabase;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz){
        this.mSqliteDatabase = sqLiteDatabase;
        this.mClazz = clazz;

        //创建表 sql语句
        //"create table if not exists Person (id integer primary key autoincrement,name text, age integer,flag boolean)"
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table if not exists ").append(DaoUtil.getTableName(clazz)).append(" (id integer primary key autoincrement,");

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            //得到int String 或者boolean 我们需要将这些转化成数据库语言类型
            String type = field.getType().getSimpleName();
            buffer.append(name).append(" ").append(DaoUtil.getColumnType(type)).append(", ");
        }
        //把stringbuffer的最后一个", ”替换成")"
        buffer.replace(buffer.length() - 2,buffer.length(),")");
        String value = buffer.toString();
        System.out.println("aaaa:"+value);
        mSqliteDatabase.execSQL(buffer.toString());
    }

    //插入数据库，t是任意对象
    @Override
    public int insert(T t) {
        return 0;
    }
}
















