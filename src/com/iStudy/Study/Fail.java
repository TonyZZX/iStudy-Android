package com.iStudy.Study;

import com.iStudy.Study.R;
import com.iStudy.Study.Support.Support;
import android.os.Bundle;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.Base.BaseActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Fail extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fail);
		Support.actionBarA(Fail.this);
        
		Button again = (Button)findViewById(R.id.again);
		again.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Support.toMain(Fail.this);
			}
		});
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Support.toMain(Fail.this);
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
    public void onBackPressed() {
    	Support.toMain(Fail.this);
    }
}