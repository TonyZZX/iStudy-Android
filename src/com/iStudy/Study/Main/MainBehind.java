package com.iStudy.Study.Main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import com.iStudy.Study.R;
import com.iStudy.Study.Base.BaseDialogFragment;
import com.iStudy.Study.Base.BaseFragment;
import com.iStudy.Study.Renren.AsyncRenren;
import com.iStudy.Study.Renren.Common.AbstractRequestListener;
import com.iStudy.Study.Renren.Exception.RenrenError;
import com.iStudy.Study.Renren.Users.UsersGetInfoRequestParam;
import com.iStudy.Study.Renren.Users.UsersGetInfoResponseBean;
import com.iStudy.Study.Support.Send;
import com.iStudy.Study.Support.Data;
import com.iStudy.Study.Support.Support;
import com.iStudy.Study.Website.Renren;
import com.iStudy.Study.Website.Sina;
import com.iStudy.Study.Website.Tencent;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainBehind extends BaseFragment {
	TextView website, info, logout, settings, update, share, feedback, about, instructions;
	Oauth2AccessToken sOAuth;
	com.iStudy.Study.Renren.Renren renren;
	String rOAuth, url, name;
	OAuthV2 tOAuth;
	Handler handler;
	int websiteID = 1;
	static EditText shareEdit, feedEdit;
	static Button internet, friend, send;
	static Intent intent;
	Drawable sitePic, drawable;
	int runnableID = 0;
	long uid;
	Bitmap bitmap, roundB;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_behind, container, false);
		sOAuth = Sina.readOAuth(getActivity());
		renren = new com.iStudy.Study.Renren.Renren(Renren.API_KEY, Renren.SECRET_KEY, Renren.APP_ID, getActivity());
		rOAuth = Renren.readUID(getActivity());
		tOAuth = Tencent.readOAuth(getActivity());
		handler = new Handler();
		
		website = (TextView) v.findViewById(R.id.website);
		handler.post(setWebsite);
		website.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new sitesDialog().show(getFragmentManager(), "Dialog");
			}
		});
		
		info = (TextView) v.findViewById(R.id.info);
		info.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Data.readMode(getActivity()) != 3) {
					switch (runnableID) {
					//有登录新浪微博
					case 1:
						handler.post(refresh);
						Sina.keepName(getActivity(), "");
						setSina();
						break;
					//没登录新浪微博
					case 2:
						Sina.login(getActivity());
						break;
						
					//有登录人人网
					case 5:
						handler.post(refresh);
						Renren.keepName(getActivity(), "");
						setRenren();
						break;
					//没登录人人网
					case 6:
						Renren.login(getActivity(), getActivity());
						break;
						
					//有登录腾讯微博
					case 7:
						handler.post(refresh);
						Tencent.keepName(getActivity(), "");
						setTencent();
						break;
					//没登录腾讯微博
					case 8:
						startActivityForResult(Tencent.login(getActivity()), 2);
						break;
					}
				} else {
					Toast.makeText(getActivity(), R.string.studying_text3, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		logout = (TextView) v.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				drawable = getResources().getDrawable(R.drawable.add);
				switch (websiteID) {
				case 1:
					runnableID = 2;
					Sina.clearOAuth(getActivity());
					break;
				case 3:
					runnableID = 6;
					Renren.clearOAuth(getActivity());
					break;
				case 4:
					runnableID = 8;
					Tencent.clearOAuth(getActivity());
					break;
				}
				handler.post(setInfo);
				Support.clearCookie(getActivity());
				toWelcome();
			}
		});
		
		settings = (TextView) v.findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(getActivity(), com.iStudy.Study.Settings.class);
				startActivity(intent);
			}
		});
		
		update = (TextView) v.findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Support.url("http://istudyapp.duapp.com/index.php", getActivity());
			}
		});
		
		share = (TextView) v.findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Data.readMode(getActivity()) != 3) {
					new ShareDialog().show(getFragmentManager(), "ShareDialog");
				} else {
					Toast.makeText(getActivity(), R.string.studying_text3, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		feedback = (TextView) v.findViewById(R.id.feedback);
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (Data.readMode(getActivity()) != 3) {
					new FeedDialog().show(getFragmentManager(), "FeedDialog");
				} else {
					Toast.makeText(getActivity(), R.string.studying_text3, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		about = (TextView) v.findViewById(R.id.about);
		about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(getActivity(), com.iStudy.Study.About.class);
				startActivity(intent);
			}
		});
		
		instructions = (TextView) v.findViewById(R.id.instructions);
		instructions.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(getActivity(), com.iStudy.Study.Instructions.Instructions.class);
				startActivity(intent);
			}
		});
		return v;
	}
	
	/**获取腾讯用户授权信息*/
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	Tencent.result(getActivity(), requestCode, resultCode, data);
    }
	
    Runnable setWebsite = new  Runnable() {
        public void run() {
        	switch (Data.readRadioID(getActivity())) {
    		case 1:
    			website.setText(R.string.sina);
    			sitePic = getResources().getDrawable(R.drawable.sina);
    			setSina();
    			break;
    		case 3:
    			website.setText(R.string.renren);
    			sitePic = getResources().getDrawable(R.drawable.renren);
    			setRenren();
    			break;
    		case 4:
    			website.setText(R.string.tencent);
    			sitePic = getResources().getDrawable(R.drawable.tencent);
    			setTencent();
    			break;
    		}
        	website.setCompoundDrawablesWithIntrinsicBounds(sitePic, null, null, null);
        }
    };
	Runnable setInfo = new  Runnable() {
        public void run() {
        	if (runnableID == 1 || runnableID == 5 || runnableID == 7) {
        		info.setText(name + "   ");
        		toRound();
        		info.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        		if (Data.readMode(getActivity()) != 3) {
        			logout.setVisibility(View.VISIBLE);
        		}
        	} else {
        		info.setText(R.string.add_website);
        		info.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        		logout.setVisibility(View.GONE);
        	}
        }
    };
    Runnable refresh = new  Runnable() {
        public void run() {
        	if (runnableID == 1 || runnableID == 5 || runnableID == 7) {
        		info.setText(R.string.website_name);
        		info.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        	}
        }
    };
    
    /**选择社交平台dialog*/
    @SuppressLint("ValidFragment")
	public class sitesDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_websites, container, false);
    		RadioGroup websites = (RadioGroup) v.findViewById(R.id.websites);
    		switch (Data.readRadioID(getActivity())) {
    		case 1:
    			websites.check(R.id.sina);
    			setSina();
    			break;
    		case 3:
    			websites.check(R.id.renren);
    			setRenren();
    			break;
    		case 4:
    			websites.check(R.id.tencent);
    			setTencent();
    			break;
    		}
    		websites.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			public void onCheckedChanged(RadioGroup group, int checkedID) {
    				switch (checkedID) {
    				case R.id.sina:
    					Data.keepRadioID(getActivity(), 1);
    					break;
    				case R.id.renren:
    					Data.keepRadioID(getActivity(), 3);
    					break;
    				case R.id.tencent:
    					Data.keepRadioID(getActivity(), 4);
    					break;
    				}
    				handler.post(setWebsite);
    				dismiss();
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
    
    /**分享dialog*/
    @SuppressLint("ValidFragment")
	public class ShareDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_share, container, false);
    		shareEdit = (EditText) v.findViewById(R.id.shareEdit);
    		internet = (Button) v.findViewById(R.id.internet);
    		internet.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if (!shareEdit.getText().toString().equals("")) {
    					dismiss();
    					Send.send(getFragmentManager(), getActivity(), shareEdit.getText().toString(), 2, getString(R.string.error));
    				} else {
    					Toast.makeText(getActivity(), R.string.write_word, Toast.LENGTH_SHORT).show();
					}
    			}
    		});
    		
    		friend = (Button) v.findViewById(R.id.friend);
    		friend.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if (!shareEdit.getText().toString().equals("")) {
    					dismiss();
    					intent=new Intent(Intent.ACTION_SEND);
        				intent.setType("text/plain");
        				intent.putExtra(Intent.EXTRA_TEXT, shareEdit.getText().toString());
        				startActivity(Intent.createChooser(intent, getString(R.string.share)));
    				} else {
    					Toast.makeText(getActivity(), R.string.write_word, Toast.LENGTH_SHORT).show();
					}
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
    
    /**反馈dialog*/
    @SuppressLint("ValidFragment")
	public class FeedDialog extends BaseDialogFragment {
    	@Override
        public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
    	}

    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.dialog_feedback, container, false);
    		feedEdit = (EditText) v.findViewById(R.id.feedEdit);
    		send = (Button) v.findViewById(R.id.send);
    		send.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if (!feedEdit.getText().toString().equals("")) {
    					dismiss();
    					Send.send(getFragmentManager(), getActivity(), feedEdit.getText().toString(), 2, getString(R.string.error));
    				} else {
    					Toast.makeText(getActivity(), R.string.write_word, Toast.LENGTH_SHORT).show();
					}
    			}
    		});
    		
    		Button cancel = (Button) v.findViewById(R.id.cancel);
    		cancel.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				dismiss();
    			}
    		});
    		return v;
    	}
    }
    
    /**设置新浪微博信息*/
    public void setSina() {
    	websiteID = 1;
		name = Sina.readName(getActivity());
		if (name.equals("")) {
			if (sOAuth.isSessionValid()) {
				uid = Sina.readUID(getActivity());
				new UsersAPI(sOAuth).show(uid, new RequestListener() {
					public void onComplete(final String response) {
						new Thread() {
				            public void run() {
				            	try {
				            		name = new JSONObject(response).getString("screen_name");
				            		Sina.keepName(getActivity(), name);
				            		url = "http://tp1.sinaimg.cn/" + uid + "/180/0/1";
				            		getHead(1);
				            		runnableID = 1;
				            		handler.post(setInfo);
				            	} catch (Exception e) {
				            		Support.showError(getActivity(), e.getMessage().toString(), false);
				            	}
				            }
						}.start();
					}
					
					public void onError(WeiboException e) {
						Support.showError(getActivity(), e.getMessage(), false);
					}
					
					public void onIOException(IOException e) {
						Support.showError(getActivity(), e.getMessage(), false);
					}
				});
			} else {
				drawable = getResources().getDrawable(R.drawable.add);
				runnableID = 2;
				handler.post(setInfo);
			}
		} else {
			try {
				FileInputStream localStream = getActivity().openFileInput("1.jpeg");
				bitmap = BitmapFactory.decodeStream(localStream);
				drawable = new BitmapDrawable(null, bitmap);
				runnableID = 1;
	    		handler.post(setInfo);
			} catch (Exception e) {
				Support.showError(getActivity(), e.getMessage().toString(), false);
			}
		}
    }
    
    /**设置人人网信息*/
    public void setRenren() {
    	websiteID = 3;
		name = Renren.readName(getActivity());
		if (name.equals("")) {
			if (!rOAuth.equals("")) {
				String[] uid = rOAuth.split(",");
				UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(uid);
				AbstractRequestListener<UsersGetInfoResponseBean> listener = new AbstractRequestListener<UsersGetInfoResponseBean>() {
					public void onComplete(final UsersGetInfoResponseBean bean) {
						new Thread() {
				            public void run() {
				            	String info = bean.toString();
								name = info.substring(info.indexOf("name") + 7, info.indexOf("sex") - 1);
			            		Renren.keepName(getActivity(), name);
								url = info.substring(info.indexOf("headurl") + 10, info.indexOf("mainurl") - 1);
			            		getHead(3);
			            		runnableID = 5;
			            		handler.post(setInfo);
				            }
						}.start();
					}
					
					public void onRenrenError(RenrenError e) {
						Support.showError(getActivity(), e.getMessage(), false);
					}
					
					public void onFault(Throwable e) {
						Support.showError(getActivity(), e.getMessage(), false);
					}
				};
				AsyncRenren asyncRenren = new AsyncRenren(renren);
				asyncRenren.getUsersInfo(param, listener);
			} else {
				drawable = getResources().getDrawable(R.drawable.add);
				runnableID = 6;
				handler.post(setInfo);
			}
		} else {
			try {
				FileInputStream localStream = getActivity().openFileInput("3.jpeg");
				bitmap = BitmapFactory.decodeStream(localStream);
				drawable = new BitmapDrawable(null, bitmap);
				runnableID = 5;
	    		handler.post(setInfo);
			} catch (Exception e) {
				Support.showError(getActivity(), e.getMessage().toString(), false);
			}
		}
    }
    
    /**设置腾讯微博信息*/
    public void setTencent() {
    	websiteID = 4;
		name = Tencent.readName(getActivity());
		if (name.equals("")) {
			if (tOAuth.getStatus() == 0) {
	    		new Thread() {
		            public void run() {
		            	UserAPI userAPI = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A);
	                    try {
	                    	String response = userAPI.info(tOAuth, "json");
	                    	name = new JSONObject(new JSONObject(response).getString("data")).getString("nick");
	                    	Tencent.keepName(getActivity(), name);
		            		url = new JSONObject(new JSONObject(response).getString("data")).getString("head") + "/0";
		            		getHead(4);
		            		runnableID = 7;
		            		handler.post(setInfo);
	                    } catch (Exception e) {
	                    	Support.showError(getActivity(), e.getMessage().toString(), false);
	                    }
	                    userAPI.shutdownConnection();
		            }
				}.start();
	    	} else {
	    		drawable = getResources().getDrawable(R.drawable.add);
	    		runnableID = 8;
	    		handler.post(setInfo);
	    	}
		} else {
			try {
				FileInputStream localStream = getActivity().openFileInput("4.jpeg");
				bitmap = BitmapFactory.decodeStream(localStream);
				drawable = new BitmapDrawable(null, bitmap);
				runnableID = 7;
	    		handler.post(setInfo);
			} catch (Exception e) {
				Support.showError(getActivity(), e.getMessage().toString(), false);
			}
		}
    }
    
    /**图片转换成圆形*/
	public void toRound() {
		float roundPx;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		roundPx = width / 2;
		top = 0;
		bottom = width;
		left = 0;
		right = width;
		height = width;
		dst_left = 0;
		dst_top = 0;
		dst_right = width;
		dst_bottom = width;
		roundB = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundB);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
		final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		drawable = new BitmapDrawable(null, roundB);
	}
    
    /**获取头像并设置大小、形状*/
	public void getHead(int ID) {
		//处理网络图片
		URL fileUrl = null;
		bitmap = null;
		try {
			fileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			Support.showError(getActivity(), e.getMessage().toString(), false);
		}
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int densityDPI = dm.densityDpi;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int newW = 180;
		int newH = 180;
		if (densityDPI < 160) {
			newW = 60;
			newH = 60;
		} else if (densityDPI < 240) {
			newW = 120;
			newH = 120;
		} else if (densityDPI < 320) {
			newW = 180;
			newH = 180;
		} else if (densityDPI < 480) {
			newW = 240;
			newH = 240;
		} else {
			newW = 360;
			newH = 360;
		}
		float scaleWidth = ((float) newW) / w;
		float scaleHeight = ((float) newH) / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		
		//保存头像
		try {
			FileOutputStream fos = getActivity().openFileOutput(ID + ".jpeg", Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Support.showError(getActivity(), e.getMessage().toString(), false);
		}
	}
	
	/**传送到Welcome*/
	public void toWelcome() {
		sOAuth = Sina.readOAuth(getActivity());
		rOAuth = Renren.readUID(getActivity());
		tOAuth = Tencent.readOAuth(getActivity());
		if (!sOAuth.isSessionValid() && rOAuth.equals("") && tOAuth.getStatus() != 0) {
			Data.keepLoginID(getActivity(), 0);
			Support.toWelcome(getActivity());
		}
	}
}