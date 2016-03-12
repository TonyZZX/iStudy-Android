package com.iStudy.Study.Main;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.iStudy.Study.R;
import com.iStudy.Study.SlidingMenu.SlidingMenu;
import com.iStudy.Study.Support.Data;

public class Main extends MainBase {
	public Main() {
		super(R.string.begin_studying);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setContentView(R.layout.frame_above);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int densityDPI = dm.densityDpi;
		if (densityDPI <= 160 && Data.readStartID(Main.this) == 0) {
			Toast.makeText(Main.this, R.string.scroll, Toast.LENGTH_LONG).show();
			Data.keepStartID(Main.this);
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.frameAbove, new MainAbove()).commit();
		setSlidingActionBarEnabled(true);
	}
}