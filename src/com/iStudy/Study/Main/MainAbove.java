package com.iStudy.Study.Main;

import java.util.ArrayList;
import com.iStudy.Study.R;
import com.iStudy.Study.Base.BaseDialogFragment;
import com.iStudy.Study.Base.BaseFragment;
import com.iStudy.Study.NumberPicker.NumberPicker;
import com.iStudy.Study.NumberPicker.NumberPicker.Formatter;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Send;
import com.iStudy.Study.Support.Support;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainAbove extends BaseFragment {
	NumberPicker hour, minute;
	CheckBox supervision;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (Data.readStartID(getActivity()) == 0) {
			PackageInfo packageInfo = null;
			try {
				packageInfo = getActivity().getPackageManager().getPackageInfo("com.miui.backup", 0);
			} catch (Exception e) {}
			if (packageInfo != null && android.os.Build.VERSION.SDK_INT > 15) {
				new ViewDialog().show(getFragmentManager(), "Dialog");
			}
			Data.keepStartID(getActivity());
		}
		
		View v = inflater.inflate(R.layout.main_above, container, false);
        hour = (NumberPicker) v.findViewById(R.id.hour);
        hour.setMinValue(0);
        hour.setMaxValue(9);
        hour.setFocusable(true);
        hour.setFocusableInTouchMode(true);
        minute = (NumberPicker) v.findViewById(R.id.minute);
        minute.setMinValue(0);
        minute.setMaxValue(59);
        minute.setFocusable(true);
        minute.setFocusableInTouchMode(true);
        minute.setFormatter(new Formatter() {
			public String format(int value) {
				String valueS = String.valueOf(value);
				switch (valueS.length()) {
				case 1:
					return "0" + valueS;
				}
				return valueS;
			}
        });
        
        TextView isOn = (TextView) v.findViewById(R.id.isOn);
		switch (Data.readMode(getActivity())) {
		case 1:
			isOn.setText(getString(R.string.insist) + " " +  getString(R.string.is_on));
			break;
		case 2:
			isOn.setText(getString(R.string.net) + " " +  getString(R.string.is_on));
			break;
		case 3:
			isOn.setText(getString(R.string.no_net) + " " +  getString(R.string.is_on));
			break;
		}
        
        supervision = (CheckBox) v.findViewById(R.id.supervision);
        if (Data.readMode(getActivity()) == 3) {
        	supervision.setVisibility(View.GONE);
        }
        
        Button start = (Button) v.findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Data.readMode(getActivity()) != 3) {
					if (!Support.checkNetwork(getActivity())) {
						Toast.makeText(getActivity(), R.string.network, Toast.LENGTH_SHORT).show();
					} else {
						if (hour.getValue() != 0 || minute.getValue() != 0) {
							ArrayList<Integer> times = new ArrayList<Integer>();
							times.add(hour.getValue());
							times.add(minute.getValue());
							Data.keepTimes(getActivity(), times);
							
							if(supervision.isChecked()){
								Send.send(getFragmentManager(), getActivity(), Support.random(getActivity()), 1, getString(R.string.error));
							} else {
								Send.toStudying(getActivity());
							}
						} else {
							Toast.makeText(getActivity(), R.string.set_minute, Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					if (hour.getValue() != 0 || minute.getValue() != 0) {
						ArrayList<Integer> times = new ArrayList<Integer>();
						times.add(hour.getValue());
						times.add(minute.getValue());
						Data.keepTimes(getActivity(), times);
						
						Send.toStudying(getActivity());
					} else {
						Toast.makeText(getActivity(), R.string.set_minute, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		return v;
	}
	
	/**悬浮窗dialog*/
    public static class ViewDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_view, container, false);
    		Button know = (Button) v.findViewById(R.id.know);
    		know.setOnClickListener(new OnClickListener() {
    			@SuppressLint("InlinedApi")
				public void onClick(View v) {
    				Toast.makeText(getActivity(), R.string.view_tip, Toast.LENGTH_LONG).show();
    				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    				intent.setData(Uri.fromParts("package", "com.iStudy.Study", null));
    				startActivity(intent);
    			}
    		});
    		return v;
    	}
    }
}