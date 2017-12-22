package com.app.rzm.ui;

import com.app.rzm.ui.fragment.FindFragment;
import com.app.rzm.ui.fragment.MessageFragment;
import com.example.mylibrary.BaseSkinActivity;
import com.example.mylibrary.navigation.CommonNavigationBar;
import com.app.rzm.R;
import com.app.rzm.ui.fragment.FragmentManagerHelper;
import com.app.rzm.ui.fragment.HomeFragment;
import com.app.rzm.ui.fragment.NewFragment;
import com.rzm.commonlibrary.inject.OnClick;

public class HomeActivity extends BaseSkinActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private NewFragment mNewFragment;
    private MessageFragment mMessageFragment;

    private FragmentManagerHelper mFragmentHelper;

    @Override
    protected void initTitle() {
        CommonNavigationBar navigationBar = new
                CommonNavigationBar.Builder(this)
                .setTitle("首页")
                .build();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }


    @Override
    protected void initData() {
        mFragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentHelper.add(mHomeFragment);
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.home_rb)
    private void homeRbClick() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragmentHelper.switchFragment(mHomeFragment);
    }

    @OnClick(R.id.find_rb)
    private void findRbClick() {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        mFragmentHelper.switchFragment(mFindFragment);
    }

    @OnClick(R.id.new_rb)
    private void newRbClick() {
        if (mNewFragment == null) {
            mNewFragment = new NewFragment();
        }
        mFragmentHelper.switchFragment(mNewFragment);
    }

    @OnClick(R.id.message_rb)
    private void messageRbClick() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        mFragmentHelper.switchFragment(mMessageFragment);
    }
}
