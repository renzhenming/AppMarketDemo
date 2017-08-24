package com.rzm.commonlibrary.general.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by renzhenming on 2017/8/22.
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    long insert(T t);

    //批量插入，检测性能
    void insert(List<T> data);

    // 查询所有
    List<T> query();

    int delete(String whereClause, String[] whereArgs);

    int update(T obj, String whereClause, String... whereArgs);
}
