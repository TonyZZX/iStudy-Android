package com.iStudy.Study.wxapi;

import com.iStudy.Study.R;
import com.iStudy.Study.Base.BaseActivity;
import com.iStudy.Study.Support.Support;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import android.os.Bundle;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank);
		Support.toMain(WXEntryActivity.this);
    }
	
	@Override
	public void onReq(BaseReq arg0) {}
	
	@Override
	public void onResp(BaseResp arg0) {}
}
