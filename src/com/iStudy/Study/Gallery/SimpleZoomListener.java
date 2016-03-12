package com.iStudy.Study.Gallery;

import android.view.MotionEvent;
import android.view.View;

public class SimpleZoomListener implements View.OnTouchListener {
	ControlType mControlType = ControlType.PAN;
	ZoomState mState;
	float mX;
	float mY;
	float mGap;
	
	public enum ControlType {
		PAN, ZOOM
    }
	
	public void setZoomState(ZoomState state) {
		mState = state;
    }
	
    public void setControlType(ControlType controlType) {
        mControlType = controlType;
    }
    
    @SuppressWarnings("deprecation")
	public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        int pointCount = event.getPointerCount();
        switch (pointCount) {
        case 1:
        	final float x = event.getX();
            final float y = event.getY();
            switch (action) {
            case MotionEvent.ACTION_DOWN:
            	mX = x;
            	mY = y;
            	break;
            case MotionEvent.ACTION_MOVE:
            	final float dx = (x - mX) / v.getWidth();
            	final float dy = (y - mY) / v.getHeight();
            	mState.setPanX(mState.getPanX() - dx);
            	mState.setPanY(mState.getPanY() - dy);
            	mState.notifyObservers();
            	mX = x;
            	mY = y;
            	break;
            }
            break;
        case 2:
        	float x0 = event.getX(event.getPointerId(0));
        	float y0 = event.getY(event.getPointerId(0));
        	float x1 = event.getX(event.getPointerId(1));
        	float y1 = event.getY(event.getPointerId(1));
        	float gap = getGap(x0, x1, y0, y1);
        	
        	switch (action) {
        	case MotionEvent.ACTION_POINTER_1_DOWN:
        		mGap = gap;
        		break;
        	case MotionEvent.ACTION_POINTER_1_UP:
        		mX = x1;
        		mY = y1;
        		break;
        	case MotionEvent.ACTION_POINTER_2_UP:
        		mX = x0;
        		mY = y0;
        		break;
        	case MotionEvent.ACTION_MOVE: 
        		float dgap = (gap - mGap)/ mGap;
        		mState.setZoom(mState.getZoom() * (float)Math.pow(5, dgap));
        		mState.notifyObservers();
        		mGap = gap;
        		break;
        	}
        }
        return true;
    }
    
    public float getGap(float x0, float x1, float y0, float y1) {
    	return (float)Math.pow(Math.pow((x0-x1), 2)+Math.pow((y0-y1), 2), 0.5);
    }
}