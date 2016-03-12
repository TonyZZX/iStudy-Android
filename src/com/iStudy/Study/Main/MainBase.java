package com.iStudy.Study.Main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.baidu.mobstat.StatService;
import com.iStudy.Study.R;
import com.iStudy.Study.ActionBarSherlock.App.ActionBar;
import com.iStudy.Study.ActionBarSherlock.View.Menu;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.SlidingMenu.SlidingMenu;
import com.iStudy.Study.SlidingMenu.App.SlidingFragmentActivity;
import com.iStudy.Study.Support.Data;

public class MainBase extends SlidingFragmentActivity {
	int title;
	MainBehind mFrag;
	public MainBase(int titleRes) {
		title = titleRes;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (Data.readSkin(this)) {
		case 1:
			setTheme(R.style.Green);
			break;
		case 2:
			setTheme(R.style.Blue);
			break;
		case 3:
			setTheme(R.style.Purple);
			break;
		case 4:
			setTheme(R.style.Orange);
			break;
		case 5:
			setTheme(R.style.Red);
			break;
		}
		
		if (android.os.Build.VERSION.SDK_INT > 10 && Data.readAcceleration(this) == 1) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		setTitle(title);
		ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        
		setBehindContentView(R.layout.frame_behind);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new MainBehind();
			t.replace(R.id.frameBehind, mFrag);
			t.commit();
		} else {
			mFrag = (MainBehind) this.getSupportFragmentManager().findFragmentById(R.id.frameBehind);
		}
		
		//滑动菜单
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.above_width);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	@Override
	public void onResume() {
		StatService.onResume(this);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		StatService.onPause(this);
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}