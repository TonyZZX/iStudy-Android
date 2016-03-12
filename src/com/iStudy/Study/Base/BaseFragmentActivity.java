package com.iStudy.Study.Base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowManager;

import com.baidu.mobstat.StatService;
import com.iStudy.Study.R;
import com.iStudy.Study.ActionBarSherlock.App.SherlockFragmentActivity;
import com.iStudy.Study.Support.Data;

@SuppressLint("InlinedApi")
public class BaseFragmentActivity extends SherlockFragmentActivity {
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
}
