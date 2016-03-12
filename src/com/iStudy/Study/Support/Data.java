package com.iStudy.Study.Support;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Data {
	public static final String DATA = "Data";
	/**保存登录状态*/
	public static void keepLoginID(Context context, int ID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("loginID", ID);
		editor.commit();
	}
	
	/**读取登录状态*/
	public static int readLoginID(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("loginID", 0);
	}
	
	/**保存启动状态*/
	public static void keepStartID(Context context) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("startID", 1);
		editor.commit();
	}
	
	/**读取启动状态*/
	public static int readStartID(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("startID", 0);
	}
	
	/**保存社交平台RadioButton*/
	public static void keepRadioID(Context context, int radioID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("radioID", radioID);
		editor.commit();
	}
	
	/**读取社交平台RadioButton*/
	public static int readRadioID(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("radioID", 1);
	}
	
	/**暂时保存times*/
	public static void keepTimes(Context context, ArrayList<Integer> times) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("status_size",times.size());
		for (int i=0; i<times.size(); i++) {
	    	editor.remove("status_" + i);
	    	editor.putInt("status_" + i, times.get(i));
		}
		editor.commit();
	}
	
	/**读取times*/
	public static ArrayList<Integer> readTimes(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		int size = pref.getInt("status_size", 0);
		ArrayList<Integer> times = new ArrayList<Integer>();
	    for (int i=0 ;i<size ;i++) {
	    	times.add(pref.getInt("status_" + i, 0));
	    }
		return times;
	}
	
	/**保存学习模式*/
	public static void keepMode(Context context, int modeID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("modeID", modeID);
		editor.commit();
	}
	
	/**读取学习模式*/
	public static int readMode(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("modeID", 1);
	}
	
	/**保存皮肤*/
	public static void keepSkin(Context context, int skinID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("skinID", skinID);
		editor.commit();
	}
	
	/**读取皮肤*/
	public static int readSkin(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("skinID", 1);
	}
	
	/**保存自定义背景*/
	public static void keepBG(Context context, int BGID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("BGID", BGID);
		editor.commit();
	}
	
	/**读取自定义背景*/
	public static int readBG(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("BGID", 0);
	}
	
	/**保存硬件加速状态*/
	public static void keepAcceleration(Context context, int accelerationID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("accelerationID", accelerationID);
		editor.commit();
	}
	
	/**读取硬件加速状态*/
	public static int readAcceleration(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("accelerationID", 0);
	}
	
	/**保存振动提醒状态*/
	public static void keepVibrate(Context context, int vibrateID) {
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putInt("vibrateID", vibrateID);
		editor.commit();
	}
	
	/**读取振动提醒状态*/
	public static int readVibrate(Context context){
		SharedPreferences pref = context.getSharedPreferences(DATA, Context.MODE_APPEND);
		return pref.getInt("vibrateID", 1);
	}
}