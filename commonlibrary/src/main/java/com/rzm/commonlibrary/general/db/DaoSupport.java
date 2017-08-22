package com.rzm.commonlibrary.general.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renzhenming on 2017/8/22.
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    private Class<T> mClazz;
    private SQLiteDatabase mSqliteDatabase;
    //缓存变量，这个只是为了显得规范，其实达不到提高效率的目的，这种写法是仿照AppCompatViewInflater写的
    private static final Object [] mPutMethodArgs = new Object[2];
    //缓存反射获取到的方法，这样如果数据量很大，那么就不需要反复的去执行反射获取方法了，达到提高效率的目的
    private static final Map<String,Method> mPutMethods = new HashMap<>();

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
    public long insert(T t) {
        //使用的还是原生的方式，我们只是封装一下
        ContentValues value = contentValueByObj(t);
        //速度比第三方快一倍
        return mSqliteDatabase.insert(DaoUtil.getTableName(mClazz),null,value);
    }

    @Override
    public void insert(List<T> data) {
        mSqliteDatabase.beginTransaction();
        for (T t : data) {
            insert(t);
        }
        mSqliteDatabase.setTransactionSuccessful();
        mSqliteDatabase.endTransaction();
    }

    private ContentValues contentValueByObj(T t) {
        ContentValues contentValues = new ContentValues();

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(t);

                mPutMethodArgs[0] = name;
                mPutMethodArgs[1] = value;

                //使用反射获取方法执行

                String filedTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null){
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(filedTypeName,putMethod);
                }

                putMethod.setAccessible(true);
                putMethod.invoke(contentValues,mPutMethodArgs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }

        }
        return contentValues;
    }
}
















