package com.iStudy.Study;

import android.os.Bundle;
import com.iStudy.Study.Base.BaseActivity;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Prepare extends BaseActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank);
		IWXAPI api = WXAPIFactory.createWXAPI(this, "wx792a4d566bec209e", false);
		api.registerApp("wx792a4d566bec209e");
		
		if (Data.readLoginID(Prepare.this) == 1) {
			Support.toMain(Prepare.this);
		} else {
			Support.toWelcome(Prepare.this);
		}
    }
}