package com.iStudy.Study.Welcome;

import com.iStudy.Study.Base.BaseFragment;
import com.iStudy.Study.R;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeWelcome extends BaseFragment {
	ViewPager viewPager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.welcome_welcome, container, false);
		v.setId(1);
		viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
		TextView right = (TextView) v.findViewById(R.id.right);
		right.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		
		Button begin = (Button) v.findViewById(R.id.begin);
        begin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				viewPager.setCurrentItem(8);
			}
		});
		return v;
	}
}