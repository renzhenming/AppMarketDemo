package com.example.renzhenming.appmarket.vm.fragment;

import android.support.v4.app.Fragment;

import com.rzm.commonlibrary.stack.BaseFragment;

/**
 * Created by renzhenming on 2017/7/25.
 */

public class FragmentFactory {
    public static Fragment getFragment(BaseFragment base ,int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new Other2Fragment();
                break;
            case 2:
                fragment = new Other3Fragment();
                break;
            case 3:
                fragment = new Other3Fragment();
                break;
            default:
                break;
        }
        return fragment;
    }
}
