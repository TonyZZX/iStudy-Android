package com.iStudy.Study;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.iStudy.Study.Base.BaseFragmentActivity;
import com.iStudy.Study.Gallery.DBAdapter;
import com.iStudy.Study.Gallery.GridViewAdapter;
import com.iStudy.Study.Gallery.ImageCommon;
import com.iStudy.Study.Gallery.ImageInfo;
import com.iStudy.Study.Gallery.ImageZoomView;
import com.iStudy.Study.Gallery.ListViewAdapter;
import com.iStudy.Study.Gallery.SimpleZoomListener;
import com.iStudy.Study.Gallery.ZoomState;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Send;
import com.iStudy.Study.Support.Support;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Studying extends BaseFragmentActivity {
	int hour, minute, rest, galleryID, studyingShow, webShow, transShow, galShow, calShow, equalID, pointID = 0;
	Count count;
	Handler handler;
	View studyingV, webV, transV, galV, calV;
	WindowManager wm;
	String hourStr, minuteStr, secondStr, random, transResult, calResult, calError = "";
	TextView time, studyingText, up, previous, refresh, next, zoomIn, zoomOut, transText, backG, backI, calResultText, calText;
	Button giveUp, transButton, add, subtract, multiply, divide, equal, delete, point, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
	GridView tools, grid;
	ArrayList<HashMap<String, Object>> gridItem;
	WebView web;
	EditText transEdit;
	ListView list;
	LinkedList<ImageInfo> bitmaps, bitImages = null;
	DBAdapter dbAdapter = null;
	Cursor cursor = null;
	LinearLayout listL, gridL, imageL;
	String[] albums = null;
	FileInputStream fis;
	Bitmap bitmap;
	String[] ids;
	ImageZoomView image;
	ZoomState zoomState;
	SimpleZoomListener zoomListener;
	Double num1, num2, resultD;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.blank);
		
		studyingV = LayoutInflater.from(this).inflate(R.layout.studying, null);
		webV = LayoutInflater.from(this).inflate(R.layout.web, null);
		transV = LayoutInflater.from(this).inflate(R.layout.translator, null);
		galV = LayoutInflater.from(this).inflate(R.layout.gallery, null);
		calV = LayoutInflater.from(this).inflate(R.layout.calculator, null);
		
		wm = (WindowManager) getApplicationContext().getSystemService("window");
        wm.addView(studyingV, Support.wmParams());
        studyingShow = 1;
        
        time = (TextView) studyingV.findViewById(R.id.time);
        Intent intent = getIntent();
		ArrayList<Integer> times = intent.getIntegerArrayListExtra("times");
		hour = times.get(0);
		minute = times.get(1);
		rest = (hour * 3600 + minute * 60) * 1000;
		count = new Count(rest, 1000);
		count.start();
		handler = new Handler();
		handler.post(setTime);
		
        studyingText = (TextView) studyingV.findViewById(R.id.studyingText);
        giveUp = (Button) studyingV.findViewById(R.id.giveUp);
        handler.post(start);
		giveUp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				count.cancel();
				random = null;
				switch ((int) (Math.random() * 7 + 1)) {
				case 1:
					random = getString(R.string.punishment1);
					break;
				case 2:
					random = getString(R.string.punishment2);
					break;
				case 3:
					random = getString(R.string.punishment3);
					break;
				case 4:
					random = getString(R.string.punishment4);
					break;
				case 5:
					random = getString(R.string.punishment5);
					break;
				case 6:
					random = getString(R.string.punishment6);
					break;
				case 7:
					random = getString(R.string.punishment7);
					break;
				}
				ArrayList<Integer> times = Data.readTimes(Studying.this);
				if (times.get(0) == 0) {
					random = random +getString(R.string.end1) + times.get(1) + getString(R.string.minute) + getString(R.string.end2);
				} else if (times.get(1) == 0) {
					random = random + getString(R.string.end1) + times.get(0) + getString(R.string.hour) + getString(R.string.end2);
				} else {
					random = random + getString(R.string.end1) + times.get(0) + getString(R.string.hour) + times.get(1) + getString(R.string.minute) + getString(R.string.end2);
				}
				Send.send(getSupportFragmentManager(), Studying.this, random, 3, getString(R.string.error));
				studyingShow = 1;
				wmRemove();
			}
		});
		
		tools = (GridView) studyingV.findViewById(R.id.tools);
		gridItem = new ArrayList<HashMap<String, Object>>();
		if (Data.readMode(Studying.this) != 3) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("tImage", R.drawable.up_web);
			map1.put("tName", getString(R.string.up_web));
			gridItem.add(map1);
	        HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("tImage", R.drawable.translator);
			map2.put("tName", getString(R.string.translator));
			gridItem.add(map2);
		}
        HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("tImage", R.drawable.gallery);
		map3.put("tName", getString(R.string.gallery));
		gridItem.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("tImage", R.drawable.calculator);
		map4.put("tName", getString(R.string.calculator));
		gridItem.add(map4);
		if (Data.readMode(Studying.this) != 3) {
			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("tImage", R.drawable.jyeoo);
			map5.put("tName", getString(R.string.jyeoo));
			gridItem.add(map5);
		}
        SimpleAdapter saImageItems = new SimpleAdapter(Studying.this, gridItem, R.layout.grid_tools, new String[] {"tImage","tName"}, new int[] {R.id.tImage,R.id.tName});
        tools.setAdapter(saImageItems);
        tools.setOnItemClickListener(new OnItemClickListener() {
        	@SuppressLint("SetJavaScriptEnabled")
			public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        		if (galleryID == 0) {
            		handler.post(gallery);
            		galleryID = 1;
        		}
        		HashMap<String, Object> map = new HashMap<String, Object>();
        		map = gridItem.get(position);
        		String name = map.get("tName").toString();
        		if (name == getString(R.string.up_web)) {
        			wm.addView(webV, Support.wmParams());
    				time = (TextView) webV.findViewById(R.id.time);
    				handler.post(setTime);
    				studyingShow = 0;
    				webShow = 1;
    				up = (TextView) webV.findViewById(R.id.up);
    				up.setOnClickListener(new clickListener());
    				
    				WebSettings settings = web.getSettings();
    		        settings.setJavaScriptEnabled(true);
    		        settings.setSaveFormData(false);
    		        settings.setSavePassword(false);
    				web.loadUrl("http://m.591up.com/");
        		} else if (name == getString(R.string.translator)) {
        			wm.addView(transV, Support.wmParams());
    				time = (TextView) transV.findViewById(R.id.time);
    				handler.post(setTime);
    				studyingShow = 0;
    				transShow = 1;
    				up = (TextView) transV.findViewById(R.id.up);
    				up.setOnClickListener(new clickListener());
        		} else if (name == getString(R.string.gallery)) {
        			wm.addView(galV, Support.wmParams());
    				time = (TextView) galV.findViewById(R.id.time);
    				handler.post(setTime);
    				studyingShow = 0;
    				galShow = 1;
    				up = (TextView) galV.findViewById(R.id.up);
    				up.setOnClickListener(new clickListener());
        		} else if (name == getString(R.string.calculator)) {
        			wm.addView(calV, Support.wmParams());
    				time = (TextView) calV.findViewById(R.id.time);
    				handler.post(setTime);
    				studyingShow = 0;
    				calShow = 1;
    				up = (TextView) calV.findViewById(R.id.up);
    				up.setOnClickListener(new clickListener());
        		} else if (name == getString(R.string.jyeoo)) {
        			wm.addView(webV, Support.wmParams());
        			time = (TextView) webV.findViewById(R.id.time);
        			handler.post(setTime);
        			studyingShow = 0;
        			webShow = 1;
        			up = (TextView) webV.findViewById(R.id.up);
        			up.setOnClickListener(new clickListener());
        			
        			WebSettings settings = web.getSettings();
        			settings.setJavaScriptEnabled(true);
        			settings.setSaveFormData(false);
        			settings.setSavePassword(false);
        			web.loadUrl("http://m.jyeoo.com/math2/");
        		}
        	}
        });
		
		//591随身学
		web = (WebView) webV.findViewById(R.id.web);
		web.setWebViewClient(new WebViewClient() {
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            view.loadUrl(url);
	            return true;
	        }
		});
		previous = (TextView) webV.findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (web.canGoBack()) {
					web.goBack();
			    }
			}
		});
		refresh = (TextView) webV.findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				web.loadUrl(web.getUrl());
			}
		});
		next = (TextView) webV.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (web.canGoForward()) {
					web.goForward();
			    }
			}
		});
		
		//翻译
		transEdit = (EditText) transV.findViewById(R.id.transEdit);
		transButton = (Button) transV.findViewById(R.id.transButton);
		transButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread() {
			        public void run() {
			        	try {
			        		if (transEdit.getText().toString().indexOf("\n") > 0) {
			        			transResult = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=UQuiAVqzHBGS1qHEtHKRjPaB&q=" + transEdit.getText().toString() + "&from=auto&to=auto";
			        			HttpPost httpPost = new HttpPost(transResult.replace(" ", "%20").replace("\n", "%0A"));
				    			HttpClient httpClient = new DefaultHttpClient();
				    			HttpResponse response = httpClient.execute(httpPost);
				    			String strResult = EntityUtils.toString(response.getEntity());
				    			JSONObject jsonObject = new JSONObject(strResult);
				    			JSONArray json = jsonObject.getJSONArray("trans_result");
				    			transResult = "";
				    			for (int i = 0; i < json.length(); i++) {
				    				JSONObject data = (JSONObject) json.get(i);
				    				transResult += data.getString("dst") + "\n";
				    			}
			        		} else {
			        			transResult = "http://fanyi.youdao.com/openapi.do?keyfrom=iStudy&key=1208682784&type=data&doctype=json&version=1.1&q=" + transEdit.getText().toString();
			        			HttpGet httpGet = new HttpGet(transResult.replace(" ", "%20"));
			        			HttpResponse response = new DefaultHttpClient().execute(httpGet);
			        			String strResult = EntityUtils.toString(response.getEntity());
				    			JSONArray json = new JSONArray("[" + strResult + "]");
				    			transResult = "";
				    			for (int i = 0; i < json.length(); i++) {
				    				JSONObject jsonObject = json.getJSONObject(i);
									String query = jsonObject.getString("query");
									transResult = query + "\n";
									String translation = jsonObject.getString("translation");
									transResult += translation + "\n\n";
									if (jsonObject.has("basic")) {
										JSONObject basic = jsonObject.getJSONObject("basic");
										if (basic.has("phonetic")) {
											String phonetic = basic.getString("phonetic");
											transResult += "<" + phonetic + ">\n";
										}
										if (basic.has("explains")) {
											String explains = basic.getString("explains");
											transResult += explains + "\n";
										}
								}
									if (jsonObject.has("web")) {
										String web = jsonObject.getString("web");
										JSONArray webString = new JSONArray("[" + web + "]");
										transResult += "\n" + getString(R.string.web_translate) + "\n";
										JSONArray webArray = webString.getJSONArray(0);
										int count = 0;
										while (!webArray.isNull(count)) {
											if (webArray.getJSONObject(count).has("key")) {
												String key = webArray.getJSONObject(count).getString("key");
												transResult += "<"+(count+1)+">" + key + "\n";
											}
											if (webArray.getJSONObject(count).has("value")) {
												String value = webArray.getJSONObject(count).getString("value");
												transResult += value + "\n";
											}
											count++;
										}
									}
				    			}
				    			transResult = transResult.replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n");
			        		}
			    			handler.post(transRun);
			        	} catch (Exception e) {}
			        }
				}.start();
			}
		});
		transText = (TextView) transV.findViewById(R.id.transText);
		
		//图库
		listL = (LinearLayout) galV.findViewById(R.id.listL);
		list = (ListView) galV.findViewById(R.id.list);
		gridL = (LinearLayout) galV.findViewById(R.id.gridL);
        grid = (GridView) galV.findViewById(R.id.gridview);
		backG = (TextView) galV.findViewById(R.id.backG);
		backG.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gridL.setVisibility(View.GONE);
				listL.setVisibility(View.VISIBLE);
			}
		});
		imageL = (LinearLayout) galV.findViewById(R.id.imageL);
		image = (ImageZoomView) galV.findViewById(R.id.image);
		backI = (TextView) galV.findViewById(R.id.backI);
		backI.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler.post(setImage);
				imageL.setVisibility(View.GONE);
				gridL.setVisibility(View.VISIBLE);
			}
		});
		zoomIn = (TextView) galV.findViewById(R.id.zoomIn);
		zoomIn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				float z = zoomState.getZoom() + 0.25f;
				zoomState.setZoom(z);
				zoomState.notifyObservers();
			}
		});
		zoomOut = (TextView) galV.findViewById(R.id.zoomOut);
		zoomOut.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				float z = zoomState.getZoom() - 0.25f;
				zoomState.setZoom(z);
				zoomState.notifyObservers();
			}
		});
		
		//计算器
		calResultText = (TextView) calV.findViewById(R.id.calResultText);
		calText = (TextView) calV.findViewById(R.id.calText);
		add = (Button) calV.findViewById(R.id.add);
    	add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signShow('+');
			}
		});
    	subtract = (Button) calV.findViewById(R.id.subtract);
    	subtract.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signShow('-');
			}
		});
    	multiply = (Button) calV.findViewById(R.id.multiply);
    	multiply.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signShow('×');
			}
		});
    	divide = (Button) calV.findViewById(R.id.divide);
    	divide.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				signShow('÷');
			}
		});
    	equal = (Button) calV.findViewById(R.id.equal);
    	equal.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				equalShow('+');
				equalShow('-');
				equalShow('×');
				equalShow('÷');
			}
		});
    	delete = (Button) calV.findViewById(R.id.delete);
    	delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!calResultText.getText().toString().equals("")) {
					if (equalID == 0) {
						calResult = calResult.subSequence(0, calResult.length() - 1).toString();
					} else {
						calResult = "";
						equalID = 0;
					}
					handler.post(calSet);
				}
			}
		});
    	delete.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
            	calResult = "";
				handler.post(calSet);
                return false;  
            }  
        });
    	point = (Button) calV.findViewById(R.id.point);
    	point.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (pointID == 0) {
					if(!(calResultText.getText().toString().equals("") || equalID == 1)) {
						pointShow('+');
						pointShow('-');
						pointShow('×');
						pointShow('÷');
						if (!(calResult.indexOf('+') > 0 || calResult.indexOf('-')  > 0 || calResult.indexOf('×') > 0 || calResult.indexOf('÷') > 0)) {
							if (!(calResult.indexOf('.') > 0)) {
								calResult = calResultText.getText().toString() + ".";
								handler.post(calSet);
							} else {
								calError = getString(R.string.point_error);
								handler.post(calTextSet);
							}
						}
					}
				} else {
					calError = getString(R.string.point_error);
					handler.post(calTextSet);
				}
			}
		});
    	b0 = (Button) calV.findViewById(R.id.b0);
    	b0.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("0");
			}
		});
    	b1 = (Button) calV.findViewById(R.id.b1);
    	b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("1");
			}
		});
    	b2 = (Button) calV.findViewById(R.id.b2);
    	b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("2");
			}
		});
    	b3 = (Button) calV.findViewById(R.id.b3);
    	b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("3");
			}
		});
    	b4 = (Button) calV.findViewById(R.id.b4);
    	b4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("4");
			}
		});
    	b5 = (Button) calV.findViewById(R.id.b5);
    	b5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("5");
			}
		});
    	b6 = (Button) calV.findViewById(R.id.b6);
    	b6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("6");
			}
		});
    	b7 = (Button) calV.findViewById(R.id.b7);
    	b7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("7");
			}
		});
    	b8 = (Button) calV.findViewById(R.id.b8);
    	b8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("8");
			}
		});
    	b9 = (Button) calV.findViewById(R.id.b9);
    	b9.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				numShow("9");
			}
		});
	}
	
	//屏蔽按键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//屏蔽按键长按
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode=event.getKeyCode();
		switch(keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return super.dispatchKeyEvent(event);
    }
	
	/**倒计时*/
	public class Count extends CountDownTimer {
		public Count(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
			this.cancel();
			if (Data.readVibrate(Studying.this) == 1) {
				((Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE)).vibrate(1000);
			}
			studyingShow = 1;
			wmRemove();
			Intent intent = new Intent(Studying.this, Success.class);
			startActivity(intent);
			finish();
		}
		@Override
		public void onTick(long millisUntilFinished) {
			hourStr = Format(String.valueOf((millisUntilFinished / 1000) / 3600));
			minuteStr = Format(String.valueOf(((millisUntilFinished / 1000) % 3600) / 60));
			secondStr = Format(String.valueOf((millisUntilFinished / 1000) % 60));
			handler.post(setTime);
		}
	}
	
	public class clickListener implements OnClickListener {
		public void onClick(View v) {
			wmRemove();
			time = (TextView) studyingV.findViewById(R.id.time);
			handler.post(setTime);
			studyingShow = 1;
		}
	}
	
	Thread gallery = new  Thread() {
        @SuppressWarnings({ "rawtypes", "unchecked" })
		public void run() {
        	bitmaps = new LinkedList<ImageInfo>();
            try {
            	dbAdapter = new DBAdapter(Studying.this);
            	dbAdapter.open();
            	try {
        			cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        			if (cursor == null) {
        				return;
        			}
        		} catch(Exception e) {
        			if (cursor != null) {
        				cursor.close();
        			}
        			return;
        		}
        		ImageInfo info = null;
        		HashMap<String, LinkedList<String>> albums = ImageCommon.getAlbumsInfo(cursor);
        		cursor.close();
        		
        		for (Iterator<?> it = albums.entrySet().iterator(); it.hasNext();) {
        			Map.Entry e = (Map.Entry)it.next();
        			LinkedList<String> album = (LinkedList<String>) e.getValue();
        			if (album != null && album.size() > 0) {
        				info = new ImageInfo();
        				info.displayName = (String) e.getKey();
        				info.picturecount = String.valueOf(album.size());
        				String id = album.get(0).split("&")[0];
        				String albumpath = album.get(0).split("&")[1];
        				albumpath = albumpath.substring(0, albumpath.lastIndexOf("/"));
        				info.icon = Thumbnails.getThumbnail(getContentResolver(), Integer.valueOf(id), Thumbnails.MICRO_KIND, new BitmapFactory.Options());
        				info.path = albumpath;
        				List list = new ArrayList();
        				for (String str: album) {
        					list.add(str);
        				}
        				info.tag = list;
        				bitmaps.add(info);
        			}
        		}
        		cursor.close();
            	if (dbAdapter != null) {
            		dbAdapter.close();
            	}
            	list.setAdapter(new ListViewAdapter(Studying.this, bitmaps));
            } catch(Exception e) {
            	if (dbAdapter != null) {
            		dbAdapter.close();
            	}
            	return;
            }
            
    		list.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
    				String name = bitmaps.get(position).displayName;
    				List listList = bitmaps.get(position).tag;
    				albums = (String[]) listList.toArray(new String[listList.size()]);
    				ids = new String[albums.length];
    				for (int i = 0; i < albums.length; i++) {
    					String albumsID = albums[i].split("&")[0];
    					ids[i] = albumsID;
    				}
    				grid.setAdapter(new GridViewAdapter(Studying.this, name, ids));
    				listL.setVisibility(View.GONE);
    				gridL.setVisibility(View.VISIBLE);
    				up = (TextView) galV.findViewById(R.id.upG);
    				up.setOnClickListener(new clickListener());
    			}
    		});
    		
    		grid.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
    				ArrayList<String> pathArray = new ArrayList<String>();
    				for (int i = 0; i < albums.length; i++) {
    					String absolutePath = albums[i].split("&")[1];
    					pathArray.add(absolutePath);
    				}
    				fis = null;
    				try {
    					fis = new FileInputStream(pathArray.get(position));
    				} catch (Exception e) {}
    				bitmap = BitmapFactory.decodeStream(fis);
    				handler.post(setImage);
    				gridL.setVisibility(View.GONE);
    				imageL.setVisibility(View.VISIBLE);
    				up = (TextView) galV.findViewById(R.id.upI);
    				up.setOnClickListener(new clickListener());
    			}
    		});
        }
    };
	
	Runnable start = new  Runnable() {
        @SuppressWarnings("deprecation")
		public void run() {
        	if (Data.readBG(Studying.this) == 1) {
    			try {
    				FileInputStream localStream = openFileInput("bg.jpeg");
    				Drawable drawable = new BitmapDrawable(null, BitmapFactory.decodeStream(localStream));
    				studyingV.setBackgroundDrawable(drawable);
    			} catch (Exception e) {}
    			time.getBackground().setAlpha(150);
    			giveUp.getBackground().setAlpha(150);
    		}
        	switch (Data.readMode(Studying.this)) {
            case 1:
            	studyingText.setText(R.string.studying_text1);
            	break;
            case 2:
            	studyingText.setText(R.string.studying_text2);
            	giveUp.setVisibility(View.VISIBLE);
            	break;
            case 3:
            	studyingText.setText(R.string.studying_text3);
            	break;
            }
        }
	};
	Runnable setTime = new  Runnable() {
        public void run() {
        	time.setText(hourStr + ":" +  minuteStr  + ":" + secondStr);
        }
	};
	Runnable transRun = new  Runnable() {
        public void run() {
        	transText.setText(transResult);
        }
	};
	Runnable setImage = new  Runnable() {
        public void run() {
        	image.setImage(bitmap);
        	zoomState = new ZoomState();
    		image.setZoomState(zoomState);
    		zoomListener = new SimpleZoomListener();
    		zoomListener.setZoomState(zoomState);
    		image.setOnTouchListener(zoomListener);
    		zoomState.setPanX(0.5f);
    		zoomState.setPanY(0.5f);
    		zoomState.setZoom(1f);
    		zoomState.notifyObservers();
    		if (bitmap != null) {
    			bitmap = null;
    		}
        }
    };
	Runnable calSet = new  Runnable() {//
        public void run() {
        	calResultText.setText(calResult);
        }
    };
    Runnable calTextSet = new  Runnable() {
        public void run() {
        	calText.setText(calError);
        }
    };
    
	/**关闭所有悬浮窗*/
	public void wmRemove() {
		if (studyingShow == 1) {
			wm.removeView(studyingV);
			studyingShow = 0;
		}
		if (webShow == 1) {
			wm.removeView(webV);
			webShow = 0;
		}
		if (transShow == 1) {
			wm.removeView(transV);
			transShow = 0;
		}
		if (galShow == 1) {
			wm.removeView(galV);
			galShow = 0;
		}
		if (calShow == 1) {
			wm.removeView(calV);
			calShow = 0;
		}
	}
	
	/**格式化时间*/
	public String Format(String time) {
		if (time.length() == 1) {
			time = "0" + time;
		}
		return time;
	}
	
	/**运算符号显示*/
	public void signShow(int c) {
		if(!(calResultText.getText().toString().equals("") || equalID == 1)) {
			if (!(calResult.indexOf('+') > 0 || calResult.indexOf('-')  > 0 || calResult.indexOf('×') > 0 || calResult.indexOf('÷') > 0)) {
				switch (c) {
				case '+':
					calResult = calResultText.getText().toString() + "+";
					break;
				case '-':
					calResult = calResultText.getText().toString() + "-";
					break;
				case '×':
					calResult = calResultText.getText().toString() + "×";
					break;
				case '÷':
					calResult = calResultText.getText().toString() + "÷";
					break;
				}
				pointID = 1;
				handler.post(calSet);
			} else {
				calError = getString(R.string.only_sign);
				handler.post(calTextSet);
			}
		}
	}
	
	/**等于显示*/
	public void equalShow(int c) {
		if (!calResultText.getText().toString().equals("") && calResult.indexOf(c) > 0) {
			num1 = Double.parseDouble(calResult.substring(0, calResult.indexOf(c)));
			if (!calResult.substring(calResult.indexOf(c) + 1).equals("")) {
				num2 = Double.parseDouble(calResult.substring(calResult.indexOf(c) + 1));
				switch (c) {
				case '+':
					resultD = num1 + num2;
					break;
				case '-':
					resultD = num1 - num2;
					break;
				case '×':
					resultD = num1 * num2;
					break;
				case '÷':
					if(num2 != 0) {
						equalID = 1;
						resultD = num1 / num2;
					} else {
						calError = getString(R.string.divisor_error);
						handler.post(calTextSet);
					}
					break;
				}
				if (c != '÷' || num2 != 0) {
					calResult = resultD.toString();
					if ((calResult.substring(calResult.indexOf('.') + 1)).length() == 1 || (calResult.substring(calResult.indexOf(c) + 1)).equals("0")) {
						calResult = calResult.substring(0, calResult.indexOf('.'));
					}
					equalID = 1;
					handler.post(calSet);
					calError = "";
					handler.post(calTextSet);
				}
			}
		}
	}
	
	/**小数点显示*/
	public void pointShow(int c) {
		if (calResult.indexOf(c) > 0) {
			if (!(calResult.substring(calResult.indexOf('.') + 1).indexOf('.') > 0)) {
				calResult = calResultText.getText().toString() + ".";
				handler.post(calSet);
			} else {
				calError = getString(R.string.point_error);
				handler.post(calTextSet);
			}
		}
	}
	
	/**数字显示*/
	public void numShow(String num) {
		if (equalID == 0) {
			calResult = calResultText.getText().toString() + num;
			pointID = 0;
		} else {
			calResult = num;
			equalID = 0;
		}
		handler.post(calSet);
	}
}