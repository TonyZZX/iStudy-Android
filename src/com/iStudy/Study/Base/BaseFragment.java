package com.iStudy.Study.Base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.WindowManager;

import com.iStudy.Study.R;
import com.iStudy.Study.ActionBarSherlock.App.SherlockDialogFragment;
import com.iStudy.Study.Support.Data;

@SuppressLint("InlinedApi")
public class BaseFragment extends SherlockDialogFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (Data.readSkin(getActivity())) {
		case 1:
			getActivity().setTheme(R.style.Green);
			break;
		case 2:
			getActivity().setTheme(R.style.Blue);
			break;
		case 3:
			getActivity().setTheme(R.style.Purple);
			break;
		case 4:
			getActivity().setTheme(R.style.Orange);
			break;
		case 5:
			getActivity().setTheme(R.style.Red);
			break;
		}
		
		if (android.os.Build.VERSION.SDK_INT > 10 && Data.readAcceleration(getActivity()) == 1) {
			getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
	}
}
