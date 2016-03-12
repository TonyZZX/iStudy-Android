package com.iStudy.Study.Website;

import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;

/**腾讯微博相关方法*/
public class Tencent {
	public static final String PREFERENCES_NAME = "Tencent";
	public static String redirectUri = "http://istudyapp.duapp.com/";
	public static String clientId = "801313720";
	public static String clientSecret = "6edb6b42f9fa464948241e92321fa08e";
	/**登录腾讯微博*/
    public static Intent login(Context context) {
    	OAuthV2 tOAuth = new OAuthV2(Tencent.redirectUri);
		tOAuth.setClientId(Tencent.clientId);
		tOAuth.setClientSecret(Tencent.clientSecret);
        OAuthV2Client.getQHttpClient().shutdownConnection();
        Intent intent = new Intent(context, com.tencent.weibo.webview.OAuthV2AuthorizeWebView.class);
        intent.putExtra("oauth", tOAuth);
		return intent;
    }
    
	/**登录腾讯微博的结果*/
    public static void result(FragmentActivity context, int requestCode, int resultCode, Intent data) {
    	if (requestCode == 2) {
            if (resultCode == OAuthV2AuthorizeWebView.RESULT_CODE) {
            	OAuthV2 tOAuth = (OAuthV2) data.getExtras().getSerializable("oauth");
                if(tOAuth.getStatus() == 0) {
                	Tencent.keepOAuth(context, tOAuth);
                	Data.keepRadioID(context, 4);
	                Data.keepLoginID(context, 1);
	        		Support.toMain(context);
                }
            }
        }
    }
    
	/**保存腾讯微博登录信息*/
	public static void keepOAuth(Context context, OAuthV2 tOAuth) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("oauth_consumer_key", tOAuth.getClientId());
		editor.putString("access_token", tOAuth.getAccessToken());
		editor.putString("openid", tOAuth.getOpenid());
		editor.putString("clientip", tOAuth.getClientIP());
		editor.putString("oauth_version", tOAuth.getOauthVersion());
		editor.putString("scope", tOAuth.getScope());
		editor.putInt("status", 0);
		editor.commit();
	}
	
	/**保存腾讯微博昵称*/
	public static void keepName(Context context, String name) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("Name", name);
		editor.commit();
	}
	
	/**读取腾讯微博登录信息*/
	public static OAuthV2 readOAuth(Context context){
		OAuthV2 tOAuth = new OAuthV2();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		tOAuth.setClientId(pref.getString("oauth_consumer_key", ""));
		tOAuth.setAccessToken(pref.getString("access_token", ""));
		tOAuth.setOpenid(pref.getString("openid", ""));
		tOAuth.setClientIP(pref.getString("clientip", ""));
		tOAuth.setOauthVersion(pref.getString("oauth_version", ""));
		tOAuth.setScope(pref.getString("scope", ""));
		tOAuth.setStatus(pref.getInt("status", 1));
		return tOAuth;
	}
	
	/**读取新浪微博昵称*/
	public static String readName(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		String name = pref.getString("Name", "");
		return name;
	}
	
	/**清空腾讯微博登录信息*/
	public static void clearOAuth(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
}