package com.example.appmarket.ui.activity;

import com.example.appmarket.R;
import com.example.appmarket.R.id;
import com.example.appmarket.R.layout;
import com.example.appmarket.R.menu;
import com.example.appmarket.ui.fragment.BaseFragment;
import com.example.appmarket.ui.fragment.FragmentFactory;
import com.example.appmarket.ui.view.PagerTab;
import com.example.appmarket.utils.UIUtils;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends BaseActivity {

	private PagerTab mPagerTab;
	private ViewPager mViewPager;
	private ActionBarDrawerToggle toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initActionBar();
	}
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("AppMarket");
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
		
		toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer_am, 0, 0);
		toggle.syncState();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void initData() {
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mPagerTab.setViewPager(mViewPager);
		mPagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment = (BaseFragment) FragmentFactory.createFragment(position);
				fragment.onLoad();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	private void initView() {
		mPagerTab = (PagerTab) findViewById(R.id.main_tab);
		mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			tabArrays = UIUtils.getStringArray(R.array.main_tab_name);
		}

		private String[] tabArrays;
		
		@Override
		public CharSequence getPageTitle(int position) {
			
			return tabArrays[position];
		}
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = FragmentFactory.createFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
		
			return tabArrays.length;
		}
		
	}

}

























