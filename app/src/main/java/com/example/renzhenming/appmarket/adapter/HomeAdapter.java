package com.example.renzhenming.appmarket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.renzhenming.appmarket.R;
import com.example.renzhenming.appmarket.utils.UIUtils;
import com.example.renzhenming.appmarket.vm.fragment.FragmentFactory;
import com.example.renzhenming.appmarket.vm.fragment.MainFragment;
import com.rzm.commonlibrary.stack.BaseFragment;

/**
 * Created by renzhenming on 2017/7/22.
 */

public class HomeAdapter extends FragmentStatePagerAdapter {


    private final BaseFragment base;

    public HomeAdapter(FragmentManager fm, BaseFragment base) {
        super(fm);
        this.base = base;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return UIUtils.getStrings(R.array.tabs)[position];
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.getFragment(base,position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
