package com.iStudy.Study.Gallery;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ImageZoomView extends View implements Observer {
	Bitmap mBitmap;
	ZoomState mState;
	float mAspectQuotient;
	
	Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	Rect mRectSrc = new Rect();
	Rect mRectDst = new Rect();
	
    public ImageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setZoomState(ZoomState state) {
        if (mState != null) {
            mState.deleteObserver(this);
        }
        mState = state;
        mState.addObserver(this);
        invalidate();
    }
    
    public void onDraw(Canvas canvas) {
    	if (mBitmap != null && mState != null) {
    		int viewWidth = getWidth();
    		int viewHeight = getHeight();
    		int bitmapWidth = mBitmap.getWidth();
    		int bitmapHeight = mBitmap.getHeight();
    		
    		float panX = mState.getPanX();
    		float panY = mState.getPanY();
    		float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
    		float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;
    		
    		//Setup source and destination rectangles
            mRectSrc.left = (int)(panX * bitmapWidth - viewWidth / (zoomX * 2));
            mRectSrc.top = (int)(panY * bitmapHeight - viewHeight / (zoomY * 2));
            mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
            mRectDst.left = getLeft();
            mRectDst.top = getTop();
            mRectDst.right = getRight();
            mRectDst.bottom = getBottom();
            
            //Adjust source rectangle so that it fits within the source image.
            if (mRectSrc.left < 0) {
                mRectDst.left += -mRectSrc.left * zoomX;
                mRectSrc.left = 0;
            }
            if (mRectSrc.right > bitmapWidth) {
                mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
                mRectSrc.right = bitmapWidth;
            }
            if (mRectSrc.top < 0) {
                mRectDst.top += -mRectSrc.top * zoomY;
                mRectSrc.top = 0;
            }
            if (mRectSrc.bottom > bitmapHeight) {
                mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
                mRectSrc.bottom = bitmapHeight;
            }
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
    	}
    }
    
    public void update(Observable observable, Object data) {
        invalidate();
    }
    
    public void calculateAspectQuotient() {
        if (mBitmap != null) {
            mAspectQuotient = (((float)mBitmap.getWidth()) / mBitmap.getHeight()) / (((float)getWidth()) / getHeight());
        }
    }
    
    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
        calculateAspectQuotient();
        invalidate();
    }
    
    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateAspectQuotient();
    }
}