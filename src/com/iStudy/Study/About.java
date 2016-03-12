package com.iStudy.Study;

import com.iStudy.Study.R;
import com.iStudy.Study.Support.Support;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.Base.BaseActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class About extends BaseActivity {
	TextView developerName, twitterName, version;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		Support.actionBarA(About.this);
        
        developerName = (TextView) findViewById(R.id.developerName);
        developerName.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Support.url("http://m.weibo.cn/123960621", About.this);
			}
		});
        twitterName = (TextView) findViewById(R.id.twitterName);
        twitterName.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Support.url("http://m.weibo.cn/u/3201410332", About.this);
			}
		});
        
        PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		version = (TextView) findViewById(R.id.version);
		version.setText("Ver. " +  packInfo.versionName);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Support.toMain(About.this);
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}