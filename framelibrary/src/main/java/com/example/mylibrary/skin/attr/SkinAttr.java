package com.example.mylibrary.skin.attr;

import android.view.View;

/**
 * Created by renzhenming on 2017/8/30.
 */

public class SkinAttr {

    private String mResourceName;
    public SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResourceName = resName;
        this.mType = skinType;
    }

    public void skin(View view) {
        mType.skin(view,mResourceName);
    }
}
