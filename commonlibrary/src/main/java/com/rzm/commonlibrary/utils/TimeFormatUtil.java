package com.rzm.commonlibrary.utils;
/**
 * Created by rzm on 2017/6/1.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class TimeFormatUtil {
    public static String getInterval(Date createAt) {
        // 定义最终返回的结果字符串。
        String interval = null;
        long millisecond = new Date().getTime() - createAt.getTime();
        long second = millisecond / 1000;
        if (second <= 0) {
            second = 0;
        }
        //*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = second + "秒以前";
        } else if (second >= 30 && second < 60) {
            interval = "半分钟前";
        } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (second >= 60 * 60 && second < 60 * 60 * 24) {//大于1小时 小于24小时
            long hour = (second / 60) / 60;
            if (hour <= 3) {
                interval = hour + "小时前";
            } else {
                interval = "今天" + getFormatTime(createAt, "HH:mm");
            }
        } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {//大于1D 小于2D
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {//大于2D小时 小于 7天
            long day = ((second / 60) / 60) / 24;
            interval = day + "天前";
        } else if ( second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) {//大于7天小于365天
            interval = getFormatTime(createAt, "MM-dd HH:mm");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        } else {
            interval = "0";
        }
        return interval;
    }
    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }

    /**
     * 把毫秒转换成：1:20:30形式
     * @param timeMs
     * @return
     */
    public static String timeFormate(long timeMs) {
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder formatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(formatBuilder, Locale.getDefault());
        formatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
