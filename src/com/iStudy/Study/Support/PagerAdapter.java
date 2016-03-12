package com.iStudy.Study.Support;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> frag;
	public PagerAdapter(FragmentManager fm, ArrayList<Fragment> frags) {
		super(fm);
		frag = frags;
	}
	
	@Override
	public int getCount() {
		return frag.size();
	}
	
	@Override
	public Fragment getItem(int item) {
		return frag.get(item);
	}
	
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
