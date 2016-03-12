package com.iStudy.Study.Support;

import java.util.ArrayList;
import com.iStudy.Study.ActionBarSherlock.App.ActionBar;
import com.iStudy.Study.Base.BaseActivity;
import com.iStudy.Study.Base.BaseFragmentActivity;
import com.iStudy.Study.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Looper;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

public class Support {
	/**传送到Main*/
	public static void toMain(Context context) {
		Intent intent = new Intent(context, com.iStudy.Study.Main.Main.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	context.startActivity(intent);
    	((Activity) context).finish();
    }
	
	/**传送到Welcome*/
	public static void toWelcome(Context context) {
		Intent intent = new Intent(context, com.iStudy.Study.Welcome.Welcome.class);
    	context.startActivity(intent);
    	((Activity) context).finish();
    }
	
	/**清空Cookie*/
	public static void clearCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager.getInstance().removeAllCookie();
    }
	
	/**检查网络连接*/
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connect = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**随机生成监督文字*/
    public static String random(Context context) {
    	String random = null;
		switch ((int) (Math.random() * 7 + 1)) {
		case 1:
			random = context.getString(R.string.supervision1);
			break;
		case 2:
			random = context.getString(R.string.supervision2);
			break;
		case 3:
			random = context.getString(R.string.supervision3);
			break;
		case 4:
			random = context.getString(R.string.supervision4);
			break;
		case 5:
			random = context.getString(R.string.supervision5);
			break;
		case 6:
			random = context.getString(R.string.supervision6);
			break;
		case 7:
			random = context.getString(R.string.supervision7);
			break;
		}
		ArrayList<Integer> times = Data.readTimes(context);
		if (times.get(0) == 0) {
			random = random + context.getString(R.string.end1) + times.get(1) + context.getString(R.string.minute) + context.getString(R.string.end2);
		} else if (times.get(1) == 0) {
			random = random + context.getString(R.string.end1) + times.get(0) + context.getString(R.string.hour) + context.getString(R.string.end2);
		} else {
			random = random + context.getString(R.string.end1) + times.get(0) + context.getString(R.string.hour) + times.get(1) + context.getString(R.string.minute) + context.getString(R.string.end2);
		}
		return random;
    }
    
    /**悬浮窗设置*/
	public static LayoutParams wmParams() {
		@SuppressWarnings("deprecation")
		LayoutParams wmParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, LayoutParams.TYPE_SYSTEM_ERROR, LayoutParams.FLAG_FULLSCREEN, PixelFormat.TRANSPARENT);
		wmParams.flags =  LayoutParams.FLAG_SHOW_WHEN_LOCKED;
		wmParams.flags =  LayoutParams.FLAG_TURN_SCREEN_ON;
		wmParams.flags =  LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL;
		return wmParams;
	}
    
    /**Activity显示ActionBar*/
    public static void actionBarA(Context context) {
    	ActionBar actionBar = ((BaseActivity) context).getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }
    
    /**FragmentActivity显示ActionBar*/
    public static void actionBarFA(Context context) {
    	ActionBar actionBar = ((BaseFragmentActivity) context).getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }
    
    /**显示错误*/
    public static void showError(Context context, String e, boolean Fail) {
        Looper.prepare();
    	Toast.makeText(context, context.getString(R.string.error) + e, Toast.LENGTH_LONG).show();
    	if (Fail) {
			Send.toFail(context);
		}
    	Looper.loop();
    }
    
    /**访问网页*/
    public static void url(String url, Context context) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}