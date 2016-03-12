package com.iStudy.Study.Website;

import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**新浪微博相关方法*/
public class Sina {
	public static final String PREFERENCES_NAME = "Sina";
	/**登录新浪微博*/
    public static void login(final Context context) {
    	Weibo weibo = Weibo.getInstance("3968019855", "http://istudyapp.duapp.com/");
		weibo.authorize(context, new WeiboAuthListener(){
	        public void onComplete(Bundle values) {
	            String token = values.getString("access_token");
	            String expires_in = values.getString("expires_in");
	            Long uid = Long.valueOf(values.getString("uid"));
	            Oauth2AccessToken sOAuth = new Oauth2AccessToken(token, expires_in);
	            if (sOAuth.isSessionValid()) {
	            	new java.util.Date(sOAuth.getExpiresTime());
	                Sina.keepOAuth(context, sOAuth);
	                Sina.keepUID(context, uid);
	                Data.keepRadioID(context, 1);
	                Data.keepLoginID(context, 1);
	        		Support.toMain(context);
	            }
	        }
	        
			public void onCancel() {}
	        public void onError(WeiboDialogError e) {}
			public void onWeiboException(WeiboException e) {}
		});
    }
    
	/**保存新浪微博登录信息*/
	public static void keepOAuth(Context context, Oauth2AccessToken sOAuth) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", sOAuth.getToken());
		editor.putLong("expiresTime", sOAuth.getExpiresTime());
		editor.commit();
	}
	
	/**保存新浪微博UID*/
	public static void keepUID(Context context, long uid) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putLong("UID", uid);
		editor.commit();
	}
	
	/**保存新浪微博昵称*/
	public static void keepName(Context context, String name) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("Name", name);
		editor.commit();
	}
	
	/**读取新浪微博登录信息*/
	public static Oauth2AccessToken readOAuth(Context context){
		Oauth2AccessToken sOAuth = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		sOAuth.setToken(pref.getString("token", ""));
		sOAuth.setExpiresTime(pref.getLong("expiresTime", 0));
		return sOAuth;
	}
	
	/**读取新浪微博UID*/
	public static long readUID(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		long uid = pref.getLong("UID", 0);
		return uid;
	}
	
	/**读取新浪微博昵称*/
	public static String readName(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		String name = pref.getString("Name", "");
		return name;
	}
	
	/**清空新浪微博登录信息*/
	public static void clearOAuth(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
}