package com.rzm.commonlibrary.general.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by renzhenming on 2017/8/22.
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    int insert(T t);

}
