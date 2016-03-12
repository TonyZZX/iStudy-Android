package com.iStudy.Study.Instructions;

import java.util.ArrayList;
import com.iStudy.Study.R;
import com.iStudy.Study.Support.PagerAdapter;
import com.iStudy.Study.Support.Support;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import com.iStudy.Study.ActionBarSherlock.View.Menu;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.Base.BaseFragmentActivity;

public class Instructions extends BaseFragmentActivity {
	ViewPager viewPager;
	int viewID;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        Support.actionBarFA(Instructions.this);
        
        Instructions1 help1 = new Instructions1();
        Instructions2 help2 = new Instructions2();
        Instructions3 help3 = new Instructions3();
        Instructions4 help4 = new Instructions4();
        Instructions5 help5 = new Instructions5();
        Instructions6 help6 = new Instructions6();
        Instructions7 help7 = new Instructions7();
        ArrayList<Fragment> frags = new ArrayList<Fragment>();
        frags.add(help1);
        frags.add(help2);
        frags.add(help3);
        frags.add(help4);
        frags.add(help5);
        frags.add(help6);
        frags.add(help7);
        
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), frags));
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				viewID = position;
			}
			public void onPageScrollStateChanged(int arg0) {}
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.instructions, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Support.toMain(Instructions.this);
        	return true;
        case R.id.previous:
        	viewPager.setCurrentItem(viewID - 1);
			return true;
        case R.id.next:
        	viewPager.setCurrentItem(viewID + 1);
			return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}