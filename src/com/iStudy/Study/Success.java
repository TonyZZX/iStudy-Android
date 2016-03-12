package com.iStudy.Study;

import java.util.ArrayList;
import com.iStudy.Study.R;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Send;
import com.iStudy.Study.Support.Support;
import android.os.Bundle;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.Base.BaseFragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class Success extends BaseFragmentActivity {
	CheckBox showOff;
	String random;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);
		Support.actionBarFA(Success.this);
		
		showOff = (CheckBox) findViewById(R.id.showOff);
		if (Data.readMode(Success.this) == 3) {
			showOff.setVisibility(View.GONE);
		}
        
		Button goOn = (Button)findViewById(R.id.goOn);
		goOn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				toMain();
			}
		});
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	toMain();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
    public void onBackPressed() {
    	toMain();
    }
    
  	/**传送到Main*/
  	public void toMain() {
  		if (Data.readMode(Success.this) != 3 && showOff.isChecked()) {
  			random = null;
			switch ((int) (Math.random() * 7 + 1)) {
			case 1:
				random = getString(R.string.show1);
				break;
			case 2:
				random = getString(R.string.show2);
				break;
			case 3:
				random = getString(R.string.show3);
				break;
			case 4:
				random = getString(R.string.show4);
				break;
			case 5:
				random = getString(R.string.show5);
				break;
			case 6:
				random = getString(R.string.show6);
				break;
			case 7:
				random = getString(R.string.show7);
				break;
			}
			ArrayList<Integer> times = Data.readTimes(Success.this);
			if (times.get(0) == 0) {
				random = random +getString(R.string.end1) + times.get(1) + getString(R.string.minute) + getString(R.string.end2);
			} else if (times.get(1) == 0) {
				random = random + getString(R.string.end1) + times.get(0) + getString(R.string.hour) + getString(R.string.end2);
			} else {
				random = random + getString(R.string.end1) + times.get(0) + getString(R.string.hour) + times.get(1) + getString(R.string.minute) + getString(R.string.end2);
			}
			Send.send(getSupportFragmentManager(), Success.this, random, 4, getString(R.string.error));
		} else {
			Support.toMain(Success.this);
		}
  	}
}