package com.example.mylibrary.skin;

import android.app.Activity;
import android.content.Context;

import com.example.mylibrary.skin.attr.SkinView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by renzhenming on 2017/8/30.
 */

public class SkinManager {

    static {
        mSkinManager = new SkinManager();
    }

    private static SkinManager mSkinManager;
    private Context mContext;
    private Map<Activity,List<SkinView>> mSkinMap = new HashMap<>();
    private SkinResource mSkinResource;


    public static SkinManager getInstance() {
        return mSkinManager;
    }

    public void init(Context context){
        //使用application context 防止内存泄漏
        mContext = context.getApplicationContext();
    }

    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        //校验签名

        //初始化资源
        mSkinResource = new SkinResource(mContext,skinPath);
        //改变皮肤
        Set<Activity> keys = mSkinMap.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViews = mSkinMap.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        return 0;
    }

    /**
     * 恢复默认
     * @return
     */
    public int restoreDefault() {

        return 0;
    }

    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinMap.get(activity);
    }

    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinMap.put(activity,skinViews);
    }

    /**
     * 获取当前的皮肤资源
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
