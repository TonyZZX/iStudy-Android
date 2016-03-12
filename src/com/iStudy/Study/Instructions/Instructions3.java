package com.iStudy.Study.Instructions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iStudy.Study.Base.BaseFragment;
import com.iStudy.Study.R;

public class Instructions3 extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.instructions_3, container, false);
		v.setId(4);
		return v;
	}
}
