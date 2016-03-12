package com.iStudy.Study.Support;

import java.util.Calendar;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

public class DigitalClock extends android.widget.DigitalClock {
    Calendar calendar;
    final static String m12 = "h:mm aa";
    final static String m24 = "k:mm";
    FormatChangeObserver mFormatChangeObserver;
    Runnable ticker;
    Handler handler;
    boolean tickerStopped = false;
    String format;
    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }
    
    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }
    
    public void initClock(Context context) {
        if (calendar == null) {
        	calendar = Calendar.getInstance();
        }
        
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();
    }
    
    @Override
    public void onAttachedToWindow() {
        tickerStopped = false;
        super.onAttachedToWindow();
        handler = new Handler();
        
        ticker = new Runnable() {
                public void run() {
                    if (tickerStopped) return;
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    setText(DateFormat.format(format, calendar));
                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    handler.postAtTime(ticker, next);
                }
            };
        ticker.run();
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        tickerStopped = true;
    }
    
    public boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }
    
    public void setFormat() {
        if (get24HourMode()) {
            format = m24;
        } else {
            format = m12;
        }
    }
    
    public class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }
        
        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }
}