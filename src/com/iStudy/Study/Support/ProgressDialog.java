package com.iStudy.Study.Support;

import com.iStudy.Study.R;
import com.iStudy.Study.Base.BaseDialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgressDialog extends BaseDialogFragment {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ProgressDialog);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.progress_bar, container, false);
		setCancelable(false);
		return v;
	}
}