package com.example.mylibrary.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.mylibrary.skin.attr.SkinAttr;
import com.example.mylibrary.skin.attr.SkinType;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renzhenming on 2017/8/30.
 * 属性解析支持
 */

public class SkinAttrSupport {

    public static final String TAG = "SkinAttrSupport";

    /**
     * 获取属性
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background textColor ...
        List<SkinAttr> skinAttrs = new ArrayList<>();

        int count = attrs.getAttributeCount();
        for (int index = 0; index < count; index++) {
            //获取属性名称 值
            String attributeName = attrs.getAttributeName(index);
            String attributeValue = attrs.getAttributeValue(index);

            //LogUtils.d(TAG,"attributeName -->"+attributeName+",attributeValue-->"+attributeValue);

            //只获取重要的属性
            SkinType skinType = getSkinType(attributeName);
            if (skinType != null){
                //资源名称，目前只有attrValue, 是一个@int类型
                String resName = getResName(context,attributeValue);
                LogUtils.d(TAG,"skinType -->"+skinType);
                LogUtils.d(TAG,"resName -->"+resName);
                if (TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }

        return skinAttrs;
    }

    /**
     * 获取资源的名称
     * @param context
     * @param attributeValue
     * @return
     */
    private static String getResName(Context context, String attributeValue) {
        if (attributeValue.startsWith("@")){
            attributeValue = attributeValue.substring(1);
            int resId = Integer.parseInt(attributeValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinType
     * @param attributeName
     * @return
     */
    private static SkinType getSkinType(String attributeName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            //返回我们需要的type类型
            if (skinType.getResName().equals(attributeName)){
                return skinType;
            }
        }
        return null;
    }
}
