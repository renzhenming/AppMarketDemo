package com.rzm.commonlibrary.general.skin.attr;

import android.view.View;

/**
 * Created by rzm on 2017/8/30.
 */

public class SkinAttr {

    public String mResourceName;
    public SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResourceName = resName;
        this.mType = skinType;
    }

    public void skin(View view) {
        mType.skin(view,mResourceName);
    }
}
