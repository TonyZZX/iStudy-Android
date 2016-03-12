package com.iStudy.Study.Website;

import com.iStudy.Study.Renren.Exception.RenrenAuthError;
import com.iStudy.Study.Renren.View.RenrenAuthListener;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**人人网相关方法*/
public class Renren {
	public static final String PREFERENCES_NAME = "Renren";
	public static final String API_KEY = "9a5e5e472dfd4a6aad98f8810183670b";
	public static final String SECRET_KEY = "b4d843eb2bf04d3eb71720d6b2c9a8dd";
	public static final String APP_ID = "230740";
	static com.iStudy.Study.Renren.Renren renren;
	/**登录人人网*/
    public static void login(final Context context, Activity activity) {
    	renren = new com.iStudy.Study.Renren.Renren(API_KEY, SECRET_KEY, APP_ID, context);
    	RenrenAuthListener listener = new RenrenAuthListener() {
        	public void onComplete(Bundle values) {
        		String token = values.getString("access_token");
	            String UID = token.substring(token.indexOf("-") + 1);
	            keepUID(context, UID);
            	Data.keepRadioID(context, 3);
                Data.keepLoginID(context, 1);
        		Support.toMain(context);
        	}
        	
        	public void onRenrenAuthError(RenrenAuthError e) {}
        	public void onCancelLogin() {}
        	public void onCancelAuth(Bundle values) {}
		};
		renren.authorize(activity, listener);
    }
	
	/**保存人人网UID*/
	public static void keepUID(Context context, String uid) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("UID", uid);
		editor.commit();
	}
	
	/**保存人人网昵称*/
	public static void keepName(Context context, String name) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("Name", name);
		editor.commit();
	}
	
	/**读取人人网UID*/
	public static String readUID(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		String uid = pref.getString("UID", "");
		return uid;
	}
	
	/**读取人人网昵称*/
	public static String readName(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		String name = pref.getString("Name", "");
		return name;
	}
	
	/**清空人人网信息*/
	public static void clearOAuth(Context context){
    	renren = new com.iStudy.Study.Renren.Renren(API_KEY, SECRET_KEY, APP_ID, context);
		renren.logout(context);
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
}