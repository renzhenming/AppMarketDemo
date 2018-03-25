package com.rzm.commonlibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.Map;

/**
 * SharedPreferences工具类
 */
public class SharePreferenceUtil {

    protected static final String SP_NAME = "SP_NAME";


    public static boolean setString(Context context, String key, String value) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, null);
    }

    public static boolean setLong(Context context, String key, long value) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getLong(key, 0L);
    }

    public static void setInt(Context context, String key, int value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
                .putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

    public static void setFloat(Context context, String key, float value) {
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
                .putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .getFloat(key, 0);
    }

    public static void setObject(Application context, String key, Object value) {
        setString(context,key,new Gson().toJson(value));
    }

    public static Object getObject(Context context, String key) {
        return new Gson().fromJson(getString(context,key),Object.class);
    }

    public static boolean setBoolean(Context context, String key, boolean value) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defualtValue) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, defualtValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static Map<String, ?> getAll(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getAll();
    }

    public static boolean clear(Context context, String key) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static boolean clearAll(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    //------------------------------------------通用------------------------------------------------

    public static void saveToSharedPreferences(Context context, String spName, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();
    }

    public static void saveToSharedPreferences(Context context, String spName, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void saveToSharedPreferences(Context context, String spName, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).commit();
    }

    public static void saveToSharedPreferences(Context context, String spName, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static void saveToSharedPreferences(Context context, String spName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static long getFromSharedPreferences(Context context, String spName, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static boolean getFromSharedPreferences(Context context, String spName, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static float getFromSharedPreferences(Context context, String spName, String key, float defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    public static int getFromSharedPreferences(Context context, String spName, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static String getFromSharedPreferences(Context context, String spName, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }
}
