package com.rzm.commonlibrary.utils;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.rzm.commonlibrary.R;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rzm on 2017/7/22.
 */
public class ToastUtil {
    private static Toast mToast;

    public static Toast getmToast() {
        return mToast;
    }

    private static Map<String, Integer> CODE_MAP = new HashMap<String, Integer>() {{
        put("101114", -1);
        /**
         * ....
         * 在这里创建code及其对应的提示语
         */
    }};

    public static String getMessage(Context context, String code) {
        Integer res = CODE_MAP.get(code);
        return context.getString(res);
    }

    public static void showCustomToast(Context context, String msg) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custome_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.custome_toast_value);
        text.setText(msg);
        if (mToast != null)
            mToast.cancel();
        mToast = new Toast(context.getApplicationContext());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(layout);
        mToast.show();
    }

    public static void showToast(Context context, int msgId) {
        showCustomToast(context, context.getString(msgId));
    }

    public static void showToast(Context context, String code) {
        showCustomToast(context, code);
    }
}