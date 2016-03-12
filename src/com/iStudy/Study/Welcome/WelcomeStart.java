package com.iStudy.Study.Welcome;

import com.iStudy.Study.R;
import com.iStudy.Study.Base.BaseFragment;
import com.iStudy.Study.Website.Renren;
import com.iStudy.Study.Website.Sina;
import com.iStudy.Study.Website.Tencent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WelcomeStart extends BaseFragment {
	View v;
	TextView sina, renren, tencent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.welcome_start, container, false);
		v.setId(9);
		sina = (TextView) v.findViewById(R.id.sina);
    	sina.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Sina.login(getActivity());
			}
		});
    	
		renren = (TextView) v.findViewById(R.id.renren);
    	renren.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Renren.login(getActivity(), getActivity());
			}
		});
    	
    	tencent = (TextView) v.findViewById(R.id.tencent);
    	tencent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		        startActivityForResult(Tencent.login(getActivity()), 2);
			}
		});
		return v;
	}
	
	/**获取腾讯微博授权信息*/
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	Tencent.result(getActivity(), requestCode, resultCode, data);
    }
}