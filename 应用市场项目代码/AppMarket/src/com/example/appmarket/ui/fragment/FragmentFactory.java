package com.example.appmarket.ui.fragment;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	//用于缓存生成的fragment，每次创建之前先在这里取，如果没有再重新创建
	private static HashMap<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>();
	
	public static Fragment createFragment(int position) {
		
		Fragment fragment = fragmentMap.get(position);
		
		if (fragment == null) {
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;

			default:
				break;
			}
			
			fragmentMap.put(position, fragment);
			
		}
		
		return fragment;
	}
}
