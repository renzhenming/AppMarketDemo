package com.example.mylibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.example.mylibrary.skin.attr.SkinView;
import com.rzm.commonlibrary.utils.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by renzhenming on 2017/8/30.
 */

public class SkinManager {

    private static final String TAG = "SkinManager";

    static {
        mSkinManager = new SkinManager();
    }

    private static SkinManager mSkinManager;
    private Context mContext;
    private Map<ISkinChangeListener,List<SkinView>> mSkinMap = new HashMap<>();
    private SkinResource mSkinResource;


    public static SkinManager getInstance() {
        return mSkinManager;
    }

    public void init(Context context){
        //使用application context 防止内存泄漏
        mContext = context.getApplicationContext();

        String skinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(skinPath)) {
            return;
        }
        File skinFile = new File(skinPath);
        if (!skinFile.exists()) {
            clearSkinInfo();
            return;
        }
        initSkinResource(skinPath);

    }
    /**
     * 初始化皮肤的Resource
     *
     * @param skinPath
     */
    private void initSkinResource(String skinPath) {
        mSkinResource = new SkinResource(mContext,skinPath);
    }

    /**
     * 清空皮肤信息
     */
    private void clearSkinInfo() {
        SkinPreUtils.getInstance(mContext).clearSkinInfo();
    }

    /** * 保存当前皮肤的信息 * * @param path 当前皮肤的路径 */
    private void saveSkinInfo(String path) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(path);
    }


    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {

        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (skinPath.equals(currentSkinPath)) {
            LogUtils.d(TAG,"已经加载过皮肤");
            return SkinConfig.SKIN_LOADED;
        }

        File skinFile = new File(skinPath);
        if(!skinFile.exists()){
            LogUtils.d(TAG,"皮肤路径错误");
            return SkinConfig.SKIN_PATH_ERROR;
        }
        // 判断签名是否正确，后面增量更新再说

        //初始化资源
        initSkinResource(skinPath);
        //改变皮肤
        changeSkin(skinPath);

        saveSkinInfo(skinPath);
        // 加载成功
        return SkinConfig.SKIN_LOAD_SUCCESS;
    }

    /** * 切换皮肤 * * @param path 当前皮肤的路径 */
    private void changeSkin(String path) {
        for (ISkinChangeListener skinChangeListener : mSkinMap.keySet()) {
            List<SkinView> skinViews = mSkinMap.get(skinChangeListener);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            skinChangeListener.changeSkin(path);
        }
    }

    /** * 恢复默认皮肤 */
    public void restoreDefault() {
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return;
        }
        String path = mContext.getPackageResourcePath();
        initSkinResource(path);
        changeSkin(path);
        clearSkinInfo();
    }


    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinMap.get(activity);
    }

    /** * 注册监听回调 */
    public void register(List<SkinView> skinViews, ISkinChangeListener skinChangeListener) {
        mSkinMap.put(skinChangeListener, skinViews);
    }

    public void changeSkin(SkinView skinView) {
        skinView.skin();
    }

    /** * 移除回调，怕引起内存泄露 */
    public void unregister(ISkinChangeListener skinChangeListener) {
        mSkinMap.remove(skinChangeListener);
    }

    /**
     * 获取当前的皮肤资源
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    public boolean needChangeSkin() {
        return SkinPreUtils.getInstance(mContext).needChangeSkin();
    }
}
