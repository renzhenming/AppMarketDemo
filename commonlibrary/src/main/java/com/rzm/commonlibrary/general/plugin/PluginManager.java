package com.rzm.commonlibrary.general.plugin;

import android.content.Context;

import com.rzm.commonlibrary.general.FixDexManager;

/**
 * Created by renzhenming on 2017/10/22.
 */

public class PluginManager {

    public static final void install(Context context,String apkPath){
        try {
            FixDexManager manager = new FixDexManager(context);
            manager.fixDex(apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
