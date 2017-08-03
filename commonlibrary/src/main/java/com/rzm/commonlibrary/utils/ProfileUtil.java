package com.rzm.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileUtil {

    /*public static void updateFromJSON(SightPlusApplication application, JSONObject json) {

        SharedPreferences.Editor editor = application.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        SightPlusApplication.User user = application.user();
        try {
            if (json.has(SightPlusApplication.User.KEY_ID)) {
                String id = json.getString(SightPlusApplication.User.KEY_ID);
                user.setId(id);
                editor.putString(SightPlusApplication.User.KEY_ID, id);
            }
            if (json.has(SightPlusApplication.User.KEY_NICK_NAME)) {
                String nickName = json.getString(SightPlusApplication.User.KEY_NICK_NAME);
                user.setNickName(nickName);
                editor.putString(SightPlusApplication.User.KEY_NICK_NAME, nickName);
            } else if(json.has("nickName")){
                String nickName = json.getString("nickName");
                user.setNickName(nickName);
                editor.putString(SightPlusApplication.User.KEY_NICK_NAME, nickName);
            }
            if (json.has(SightPlusApplication.User.KEY_AVATAR)) {
                String avatar = json.getString(SightPlusApplication.User.KEY_AVATAR);
                user.setAvatar(avatar);
                editor.putString(SightPlusApplication.User.KEY_AVATAR, avatar);
            }
            if (json.has(SightPlusApplication.User.KEY_MOBILE)) {
                String mobile = json.getString(SightPlusApplication.User.KEY_MOBILE);
                if (mobile != null && mobile.length() > 0) {
                    user.setMobile(mobile);
                    editor.putString(SightPlusApplication.User.KEY_MOBILE, mobile);
                }
            }
            if (json.has(SightPlusApplication.User.KEY_USERNAME)) {
                String username = json.getString(SightPlusApplication.User.KEY_USERNAME);
                user.setUsername(username);
                editor.putString(SightPlusApplication.User.KEY_USERNAME, username);
            }
            if (json.has(SightPlusApplication.User.KEY_TOKEN)) {
                String token = json.getString(SightPlusApplication.User.KEY_TOKEN);
                user.setToken(token);
                editor.putString(SightPlusApplication.User.KEY_TOKEN, token);
            }
            if (json.has(SightPlusApplication.User.KEY_PASSWORD)) {
                String password = json.getString(SightPlusApplication.User.KEY_PASSWORD);
                user.setPassword(password);
                editor.putString(SightPlusApplication.User.KEY_PASSWORD, password);
            }
            if (json.has(SightPlusApplication.User.KEY_IS_CHANGED)) {
                String is_changed = json.getString(SightPlusApplication.User.KEY_IS_CHANGED);
                user.setIs_changed(is_changed);
                editor.putString(SightPlusApplication.User.KEY_IS_CHANGED, is_changed);
            }
            if (json.has(SightPlusApplication.User.KEY_IS_WX_BIND)) {
                String is_wx_bind = json.getString(SightPlusApplication.User.KEY_IS_WX_BIND);
                user.setIs_wx_bind(is_wx_bind);
                editor.putString(SightPlusApplication.User.KEY_IS_WX_BIND, is_wx_bind);
            }
            if (json.has(SightPlusApplication.User.KEY_IS_QQ_BIND)) {
                String is_qq_bind = json.getString(SightPlusApplication.User.KEY_IS_QQ_BIND);
                user.setIs_qq_bind(is_qq_bind);
                editor.putString(SightPlusApplication.User.KEY_IS_QQ_BIND, is_qq_bind);
            }
            if (json.has(SightPlusApplication.User.KEY_IS_SINA_BIND)) {
                String is_sina_bind = json.getString(SightPlusApplication.User.KEY_IS_SINA_BIND);
                user.setIs_sina_bind(is_sina_bind);
                editor.putString(SightPlusApplication.User.KEY_IS_SINA_BIND, is_sina_bind);
            }
            if (json.has(SightPlusApplication.User.KEY_ISEXISTPWD)) {
                String isExitPwd = json.getString(SightPlusApplication.User.KEY_ISEXISTPWD);
                user.setIsExistPwd(isExitPwd);
                editor.putString(SightPlusApplication.User.KEY_ISEXISTPWD, isExitPwd);
            }
            if (json.has(SightPlusApplication.User.KEY_SIGNATURE)) {
                String signature = json.getString(SightPlusApplication.User.KEY_SIGNATURE);
                user.setSignature(signature);
                editor.putString(SightPlusApplication.User.KEY_SIGNATURE, signature);
            }
            if (json.has(SightPlusApplication.User.KEY_GENDER)) {
                String gender = json.getString(SightPlusApplication.User.KEY_GENDER);
                user.setGender(gender);
                editor.putString(SightPlusApplication.User.KEY_GENDER, gender);
            }
            if (json.has(SightPlusApplication.User.KEY_BIRTHDAY)) {
                String birthDay = json.getString(SightPlusApplication.User.KEY_BIRTHDAY);
                user.setBirthDay(birthDay);
                editor.putString(SightPlusApplication.User.KEY_BIRTHDAY, birthDay);
            }
            if (json.has(SightPlusApplication.User.KEY_ISEXISTNEWMESSAGE)) {
                String pwdStatus = json.getString(SightPlusApplication.User.KEY_ISEXISTNEWMESSAGE);
                user.setPwdStatus(pwdStatus);
                editor.putString(SightPlusApplication.User.KEY_ISEXISTNEWMESSAGE, pwdStatus);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        editor.apply();
    }

    *//**
     * 记录头像更新的毫秒值，这样做是为了解决更新头像后Glide设置旧头像的问题
     * @param application
     * @param modifyTime
     *//*
    public static void updateHeadImageModifyTime(SightPlusApplication application, String modifyTime) {
        SharedPreferences.Editor editor = application.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        SightPlusApplication.User user = application.user();
        user.setModify_time(modifyTime);
        editor.putString(SightPlusApplication.User.KEY_MODIFY_TIME, modifyTime);
        editor.apply();
    }

    *//**
     * 获取当前头像修改的毫秒值作为signature
     * @param application
     *//*
    public static String getHeadImageModifyTime(SightPlusApplication application) {
        SharedPreferences preferences = application.getSharedPreferences("user", Context.MODE_PRIVATE);
        String modifyTimeResult = preferences.getString(SightPlusApplication.User.KEY_MODIFY_TIME, null);
        return modifyTimeResult;
    }

    *//**
     * 获取用户某一信息
     * @param application
     * @param key
     *//*
    public static String getUserInfo(SightPlusApplication application,String key) {
        SharedPreferences preferences = application.getSharedPreferences("user", Context.MODE_PRIVATE);
        String value = preferences.getString(key, null);
        return value;
    }

    public static void deleteProfile(SightPlusApplication application) {

        SightPlusApplication.User user = application.user();
        user.setId("");
        user.setUsername("");
        user.setToken("");
        user.setNickName("");
        user.setAvatar("");
        user.setMobile("");
        user.setPassword("");
        user.setIs_changed("");
        user.setIs_wx_bind("");
        user.setIs_qq_bind("");
        user.setIs_sina_bind("");
        user.setIsExistPwd("");
        user.setGender("");
        user.setSignature("");
        user.setBirthDay("");
        user.setModify_time("");
        user.setStartPage(SightPlusApplication.StartPage.DEFAULT);

        SharedPreferences.Editor editor = application.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        editor.remove(SightPlusApplication.User.KEY_ID);
        editor.remove(SightPlusApplication.User.KEY_USERNAME);
        editor.remove(SightPlusApplication.User.KEY_NICK_NAME);
        editor.remove(SightPlusApplication.User.KEY_TOKEN);
        editor.remove(SightPlusApplication.User.KEY_AVATAR);
        editor.remove(SightPlusApplication.User.KEY_MOBILE);
        editor.remove(SightPlusApplication.User.KEY_START_PAGE);
        editor.remove(SightPlusApplication.User.KEY_PASSWORD);
        editor.remove(SightPlusApplication.User.KEY_IS_CHANGED);
        editor.remove(SightPlusApplication.User.KEY_IS_WX_BIND);
        editor.remove(SightPlusApplication.User.KEY_IS_QQ_BIND);
        editor.remove(SightPlusApplication.User.KEY_IS_SINA_BIND);
        editor.remove(SightPlusApplication.User.KEY_ISEXISTPWD);
        editor.remove(SightPlusApplication.User.KEY_GENDER);
        editor.remove(SightPlusApplication.User.KEY_SIGNATURE);
        editor.remove(SightPlusApplication.User.KEY_BIRTHDAY);
        editor.remove(SightPlusApplication.User.KEY_MODIFY_TIME);
        editor.remove(SightPlusApplication.User.KEY_ISEXISTNEWMESSAGE);
        editor.apply();
    }

    public static void getProfile(SightPlusApplication application, Activity thisActivity, RequestUtil.FJRequestSuccessCallback successCallback) {

        SightPlusApplication.User user = application.user();
        FJRequestHandler.Builder builder = new FJRequestHandler.Builder(application, thisActivity, RequestWrapper.Get_Profile + "?token=" + user.getToken());
        builder.successCallback(successCallback).request();
    }*/
}
