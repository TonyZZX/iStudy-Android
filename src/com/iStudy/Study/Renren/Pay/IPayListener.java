package com.iStudy.Study.Renren.Pay;

import com.iStudy.Study.Renren.Exception.RenrenError;
import com.iStudy.Study.Renren.Pay.Bean.PayOrder;
import com.iStudy.Study.Renren.Pay.Bean.Payment;

public interface IPayListener {
	public void onStart(Payment o);

	public boolean onComplete(PayOrder o);

	public void onError(RenrenError error);
}
