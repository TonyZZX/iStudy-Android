package com.iStudy.Study.Support;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.iStudy.Study.R;
import com.iStudy.Study.Renren.AsyncRenren;
import com.iStudy.Study.Renren.Common.AbstractRequestListener;
import com.iStudy.Study.Renren.Exception.RenrenError;
import com.iStudy.Study.Renren.Status.StatusSetRequestParam;
import com.iStudy.Study.Renren.Status.StatusSetResponseBean;
import com.iStudy.Study.Website.Renren;
import com.iStudy.Study.Website.Sina;
import com.iStudy.Study.Website.Tencent;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

/**发送到网络*/
public class Send {
	static ProgressDialog pd;
	static boolean dialogShow = false;
	static int cSina, cRenren, cTencent = 0;
	/**发送到网络*/
	public static void send(FragmentManager fm, final Context context, final String msg, final int ID, final String error) {
		if (android.os.Build.VERSION.SDK_INT <= 15 || ID != 3) {
			pd = new ProgressDialog();
			pd.show(fm, "Progressbar");
			dialogShow = true;
		}
		Oauth2AccessToken sOAuth = Sina.readOAuth(context);
		String rOAuth = Renren.readUID(context);
		final OAuthV2 tOAuth = Tencent.readOAuth(context);
		
		if (sOAuth.isSessionValid()) {
			new StatusesAPI(sOAuth).update(msg, null, null, new RequestListener() {
				public void onComplete(String arg0) {
					cSina = 1;
					remove();
    				if (cRenren == 1 && cTencent == 1) {
    					switch (ID) {
    					//接受监督
    					case 1:
    						toStudying(context);
    						break;
    					//分享到网络
    					case 2:
    						showToast(context);
    						break;
    					//学习失败
    					case 3:
    						toFail(context);
    						break;
    					//学习成功
    					case 4:
    						Support.toMain(context);
    						break;
    					}
    				}
				}
				
				public void onError(WeiboException e) {
					cSina = 1;
					remove();
					if (ID == 3) {
						Support.showError(context, e.getMessage(), true);
					} else {
						Support.showError(context, e.getMessage(), false);
					}
				}
				
				public void onIOException(IOException e) {
					cSina = 1;
					remove();
					if (ID == 3) {
						Support.showError(context, e.getMessage(), true);
					} else {
						Support.showError(context, e.getMessage(), false);
					}
				}
			});
		} else {
			cSina = 1;
    	}
		
		if (!rOAuth.equals("")) {
			com.iStudy.Study.Renren.Renren renren = new com.iStudy.Study.Renren.Renren(Renren.API_KEY, Renren.SECRET_KEY, Renren.APP_ID, context);
			StatusSetRequestParam param = new StatusSetRequestParam(msg);
			StatusSetListener listener = new StatusSetListener(context, ID);
			try {
				AsyncRenren aRenren = new AsyncRenren(renren);
				aRenren.publishStatus(param, listener, true);
			} catch (Throwable e) {
				Support.showError(context, e.getMessage(), false);
			}
		} else {
			cRenren = 1;
    	}
		
		if (tOAuth.getStatus() == 0) {
			new Thread() {
				public void run() {
					TAPI tAPI= new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
					try {
	                    tAPI.add(tOAuth, "json", msg, tOAuth.getClientIP());
	                    cTencent = 1;
	                    remove();
		    			if (cSina == 1 && cRenren == 1 ) {
		    				switch (ID) {
		    				//接受监督
	    					case 1:
	    						toStudying(context);
	    						break;
	    					//分享到网络
	    					case 2:
	    						showToast(context);
	    						break;
	    					//学习失败
	    					case 3:
	    						toFail(context);
	    						break;
	    					//学习成功
	    					case 4:
	    						Support.toMain(context);
	    						break;
	    					}
		    			}
	                } catch (Exception e) {
	                	cTencent = 1;
	                	remove();
	                	if (ID == 3) {
							Support.showError(context, e.getMessage().toString(), true);
						} else {
							Support.showError(context, e.getMessage().toString(), false);
						}
	                }
	                tAPI.shutdownConnection();
				}
			}.start();
		} else {
			cTencent = 1;
    	}
	}
	
	/**监听异步调用人人网发送状态接口的响应*/
	public static class StatusSetListener extends AbstractRequestListener<StatusSetResponseBean> {
		Context context;
		int ID;
		public StatusSetListener(Context context, int ID) {
			this.context = context;
			this.ID = ID;
		}
		public void onComplete(StatusSetResponseBean bean) {
			cRenren = 1;
			remove();
			if (cSina == 1 && cTencent == 1) {
				switch (ID) {
				//接受监督
				case 1:
					toStudying(context);
					break;
				//分享到网络
				case 2:
					showToast(context);
					break;
				//学习失败
				case 3:
					toFail(context);
					break;
				//学习成功
				case 4:
					Support.toMain(context);
					break;
				}
			}
		}
		public void onRenrenError(RenrenError e) {
			cRenren = 1;
			remove();
			if (ID == 3) {
				Support.showError(context, e.toString(), true);
			} else {
				Support.showError(context, e.toString(), false);
			}
		}
		public void onFault(Throwable e) {
			cRenren = 1;
			remove();
			if (ID == 3) {
				Support.showError(context, e.getMessage(), true);
			} else {
				Support.showError(context, e.getMessage(), false);
			}
		}
	}
	
	/**移除ProgressDialog*/
	public static void remove() {
		if (dialogShow) {
			if (cSina == 1 && cRenren == 1 && cTencent == 1) {
				pd.dismiss();
				dialogShow = false;
			}
		}
	}
	
	/**传送到Studying*/
	public static void toStudying(Context context) {
		ArrayList<Integer> times = Data.readTimes(context);
		Intent intent = new Intent(context, com.iStudy.Study.Studying.class);
		intent.putIntegerArrayListExtra("times", times);
		context.startActivity(intent);
		((Activity) context).finish();
	}
	
	/**分享成功Toast*/
	public static void showToast(Context context) {
		Looper.prepare();
		Toast.makeText(context, R.string.success_share, Toast.LENGTH_LONG).show();
		Looper.loop();
	}
	
	/**传送到Fail*/
	public static void toFail(Context context) {
		if (cSina == 1 && cRenren == 1 && cTencent == 1) {
			Intent intent = new Intent(context, com.iStudy.Study.Fail.class);
			context.startActivity(intent);
			((Activity) context).finish();
		}
	}
}