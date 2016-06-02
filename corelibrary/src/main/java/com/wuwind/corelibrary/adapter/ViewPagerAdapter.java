package com.wuwind.corelibrary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;

	public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	/**
	 * 得到每个页面
	 */
	@Override
	public Fragment getItem(int arg0) {
		return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
	}

	/**
	 * 页面的总个数
	 */
	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

}
