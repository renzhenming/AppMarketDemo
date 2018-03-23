package com.rzm.mylibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by rzm on 2017/8/30.
 */

public class SkinView {

    public View mView;

    public List<SkinAttr> mAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrList) {
        this.mView = view;
        this.mAttrs = skinAttrList;
    }

    public void skin(){
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }
}
