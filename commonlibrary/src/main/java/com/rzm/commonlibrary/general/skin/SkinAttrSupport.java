package com.rzm.commonlibrary.general.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.rzm.commonlibrary.general.skin.attr.SkinAttr;
import com.rzm.commonlibrary.general.skin.attr.SkinType;
import com.rzm.commonlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzm on 2017/8/30.
 * 属性解析支持
 */

public class SkinAttrSupport {

    public static final String TAG = "SkinAttrSupport";

    /**
     * 获取属性
     * @param context
     * @param attrs
     * attributeName -->layout_width, attributeValue-->-2
     * attributeName -->layout_height, attributeValue-->-2
     * attributeName -->text, attributeValue-->点击
     * attributeName -->textColor, attributeValue-->#ff000000
     * attributeName -->id, attributeValue-->@2131624078
     * attributeName -->layout_width, attributeValue-->-2
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background textColor ...
        List<SkinAttr> skinAttrs = new ArrayList<>();

        //获取的是当前view的attr属性的数量
        int count = attrs.getAttributeCount();
        LogUtils.e(TAG,"attributeCount -> "+count);
        for (int index = 0; index < count; index++) {
            //获取每一个属性的名称 值
            String attributeName = attrs.getAttributeName(index);
            //值的格式是一个 @+数字 类型
            String attributeValue = attrs.getAttributeValue(index);
            LogUtils.d(TAG,"attributeName -->"+attributeName+",attributeValue-->"+attributeValue);
            //只获取我们需要的属性，已经在SkinType中指定了，只获取换肤需要的（这里我们需要的只是textColor，background，src）
            //所以我们从众多属性中过滤出这些需要的
            SkinType skinType = getSkinType(attributeName);
            if (skinType != null){
                //根据资源的attributeValue也就是资源的id获取到资源的名称（这个资源的名称指的是例如，我设置了一张图片
                // src = "@drawable/logo"，那么我们获取到的resName 就是logo）
                String resName = getResName(context,attributeValue);
                LogUtils.d(TAG,"skinType -->"+skinType);
                LogUtils.d(TAG,"resName -->"+resName);
                if (TextUtils.isEmpty(resName)){
                    continue;
                }
                //skinType: textColor，background，src ...   resName:资源名称 如 logo
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }

        //这个集合指的是每一个view中需要换肤设置的属性的集合，比如一个ImageView中包含的src background,
        //一个view对应一个集合
        LogUtils.e(TAG,"遍历这个list集合 ");
        for (SkinAttr skinAttr : skinAttrs) {
            LogUtils.e(TAG,"SkinAttr.mResourceName："+skinAttr.mResourceName);
            LogUtils.e(TAG,"SkinAttr.mType："+skinAttr.mType);
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
