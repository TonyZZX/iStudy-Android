package com.iStudy.Study;

import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.iStudy.Study.ActionBarSherlock.View.MenuItem;
import com.iStudy.Study.Base.BaseDialogFragment;
import com.iStudy.Study.Base.BaseFragmentActivity;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;

public class Settings extends BaseFragmentActivity {
	TextView mTitle, mKey, sTitle, sKey, bgTitle, bgKey;
	Handler handler;
	CheckBox acceleration, vibrate;
	Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		Support.actionBarFA(Settings.this);
		
		mTitle = (TextView) findViewById(R.id.mTitle);
		mTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new ModeDialog().show(getSupportFragmentManager(), "ModeDialog");
			}
		});
		
		mKey = (TextView) findViewById(R.id.mKey);
		handler = new Handler();
		handler.post(setMode);
		
		sTitle = (TextView) findViewById(R.id.sTitle);
		sTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new SkinDialog().show(getSupportFragmentManager(), "SkinDialog");
			}
		});
		
		sKey = (TextView) findViewById(R.id.sKey);
		handler.post(setSkin);
		
		bgTitle = (TextView) findViewById(R.id.bgTitle);
		bgTitle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new BGDialog().show(getSupportFragmentManager(), "BGDialog");
			}
		});
		
		bgKey = (TextView) findViewById(R.id.bgKey);
		handler.post(setBG);
		
		acceleration = (CheckBox) findViewById(R.id.acceleration);
		handler.post(setAcceleration);
		acceleration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (acceleration.isChecked()) {
					Data.keepAcceleration(Settings.this, 1);
				} else {
					Data.keepAcceleration(Settings.this, 0);
				}
			}
		});
		
		vibrate = (CheckBox) findViewById(R.id.vibrate);
		handler.post(setVibrate);
		vibrate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (vibrate.isChecked()) {
					Data.keepVibrate(Settings.this, 1);
				} else {
					Data.keepVibrate(Settings.this, 0);
				}
			}
		});
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Support.toMain(Settings.this);
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
    
    public void onBackPressed() {
    	Support.toMain(Settings.this);
    }
    
    @Override  
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = Settings.this.getContentResolver();
            try {
            	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            	FileOutputStream fos = openFileOutput("bg.jpeg", Context.MODE_PRIVATE);
            	bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    			fos.flush();
    			fos.close();
    			Data.keepBG(Settings.this, 1);
    			handler.post(setBG);
    			if (bitmap != null) {
    				bitmap.recycle();
    	    	}
            } catch (Exception e) {
            	Support.showError(Settings.this, e.getMessage().toString(), true);
            }
        }
    }
    
    Runnable setMode = new  Runnable() {
        public void run() {
        	switch (Data.readMode(Settings.this)) {
    		case 1:
    			mKey.setText(R.string.insist);
    			break;
    		case 2:
    			mKey.setText(R.string.net);
    			break;
    		case 3:
    			mKey.setText(R.string.no_net);
    			break;
    		}
        }
    };
    Runnable setSkin = new  Runnable() {
        public void run() {
        	switch (Data.readSkin(Settings.this)) {
    		case 1:
    			sKey.setText(R.string.green);
    			break;
    		case 2:
    			sKey.setText(R.string.blue);
    			break;
    		case 3:
    			sKey.setText(R.string.purple);
    			break;
    		case 4:
    			sKey.setText(R.string.orange);
    			break;
    		case 5:
    			sKey.setText(R.string.red);
    			break;
        	}
        }
    };
    Runnable setBG = new  Runnable() {
        public void run() {
        	switch (Data.readBG(Settings.this)) {
    		case 0:
    			bgKey.setText(R.string.none);
    			break;
    		case 1:
    			bgKey.setText(R.string.local);
    			break;
    		}
        }
    };
    Runnable setAcceleration = new  Runnable() {
        public void run() {
        	switch (Data.readAcceleration(Settings.this)) {
    		case 0:
    			acceleration.setChecked(false);
    			break;
    		case 1:
    			acceleration.setChecked(true);
    			break;
    		}
        }
    };
    Runnable setVibrate = new  Runnable() {
        public void run() {
        	switch (Data.readVibrate(Settings.this)) {
    		case 0:
    			vibrate.setChecked(false);
    			break;
    		case 1:
    			vibrate.setChecked(true);
    			break;
    		}
        }
    };
    
    /**学习模式dialog*/
    @SuppressLint("ValidFragment")
	public class ModeDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_modes, container, false);
    		RadioGroup modes = (RadioGroup) v.findViewById(R.id.modes);
    		switch (Data.readMode(getActivity())) {
    		case 1:
    			modes.check(R.id.insist);
    			break;
    		case 2:
    			modes.check(R.id.net);
    			break;
    		case 3:
    			modes.check(R.id.noNet);
    			break;
    		}
    		modes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			public void onCheckedChanged(RadioGroup group, int checkedID) {
    				switch (checkedID) {
    				case R.id.insist:
    					Data.keepMode(getActivity(), 1);
    					Toast.makeText(getActivity(), R.string.insist_key, Toast.LENGTH_SHORT).show();
    					break;
    				case R.id.net:
    					Data.keepMode(getActivity(), 2);
    					Toast.makeText(getActivity(), R.string.net_key, Toast.LENGTH_SHORT).show();
    					break;
    				case R.id.noNet:
    					Data.keepMode(getActivity(), 3);
    					Toast.makeText(getActivity(), R.string.no_net_key, Toast.LENGTH_SHORT).show();
    					break;
    				}
    				handler.post(setMode);
    				dismiss();
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
    
    /**皮肤dialog*/
    @SuppressLint("ValidFragment")
	public class SkinDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_skins, container, false);
    		RadioGroup skins = (RadioGroup) v.findViewById(R.id.skins);
    		switch (Data.readSkin(getActivity())) {
    		case 1:
    			skins.check(R.id.green);
    			break;
    		case 2:
    			skins.check(R.id.blue);
    			break;
    		case 3:
    			skins.check(R.id.purple);
    			break;
    		case 4:
    			skins.check(R.id.orange);
    			break;
    		case 5:
    			skins.check(R.id.red);
    			break;
    		}
    		skins.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			public void onCheckedChanged(RadioGroup group, int checkedID) {
    				switch (checkedID) {
    				case R.id.green:
    					Data.keepSkin(getActivity(), 1);
    					break;
    				case R.id.blue:
    					Data.keepSkin(getActivity(), 2);
    					break;
    				case R.id.purple:
    					Data.keepSkin(getActivity(), 3);
    					break;
    				case R.id.orange:
    					Data.keepSkin(getActivity(), 4);
    					break;
    				case R.id.red:
    					Data.keepSkin(getActivity(), 5);
    					break;
    				}
    				intent = new Intent(getActivity(), com.iStudy.Study.Settings.class);
    				Settings.this.startActivity(intent);
    				Settings.this.finish();
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
    
    /**自定义背景dialog*/
    @SuppressLint("ValidFragment")
	public class BGDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_bg, container, false);
    		Button local = (Button) v.findViewById(R.id.local);
    		local.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    				intent = new Intent(Intent.ACTION_GET_CONTENT);
    				intent.addCategory(Intent.CATEGORY_OPENABLE);
    				intent.setType("image/*");
    				Settings.this.startActivityForResult(intent, 1);
    				Toast.makeText(getActivity(), R.string.local_text, Toast.LENGTH_SHORT).show();
    			}
    		});
    		
    		Button none = (Button) v.findViewById(R.id.none);
    		none.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    				Data.keepBG(Settings.this, 0);
    				handler.post(setBG);
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
}