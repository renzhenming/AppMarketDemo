package com.rzm.commonlibrary.general.navigationbar;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by renzhenming on 2018/4/2.
 */

public class ToolbarStyleNavigation extends AbsNavigationBar {

    public ToolbarStyleNavigation(Builder.AbsNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return 0;
    }

    @Override
    public void applyView() {
        //setText(R.id.title,getParams().mTitle);
    }

    public static class Builder extends AbsNavigationBar.Builder{


        private final NavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new ToolbarStyleNavigation.Builder.NavigationParams(context, parent);
        }

        // 1. 设置所有效果

        public ToolbarStyleNavigation.Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        @Override
        public AbsNavigationBar build() {
            return new ToolbarStyleNavigation(P);
        }

        public static class NavigationParams extends AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;

            public NavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
