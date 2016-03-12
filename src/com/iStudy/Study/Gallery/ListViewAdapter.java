package com.iStudy.Study.Gallery;

import java.io.File;
import java.util.LinkedList;
import com.iStudy.Study.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater mInflater;
	LinkedList<ImageInfo> imageInfo;
	public ListViewAdapter(Context activity, LinkedList<ImageInfo> imageInfos) {
		context = activity;
		mInflater = LayoutInflater.from(activity);
		imageInfo = imageInfos;
	}
	
	public int getCount() {
		return imageInfo.size();
	}
	
	public Object getItem(int position) {
	    return imageInfo.get(position);
	}
	
	public long getItemId(int position) {
	    return position;
	}
	
	public View getView(int position,View convertView,ViewGroup parent) {
	    ViewHolder holder;
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_gallery, null);
	      holder = new ViewHolder();
	      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	      holder.name = (TextView) convertView.findViewById(R.id.name);
	      holder.picturecount = (TextView) convertView.findViewById(R.id.picturecount);
	      convertView.setTag(holder);
	    } else {
	    	holder = (ViewHolder) convertView.getTag();
	    }
	    
	    if (imageInfo == null) {
	    	return convertView;
	    }
	    if (imageInfo.get(position) == null) {
	    	return convertView;
	    }
	    File f = new File(imageInfo.get(position).path);
    	String fName = f.getName();
    	holder.icon.setImageBitmap(imageInfo.get(position).icon);
    	holder.name.setText(fName);
    	holder.picturecount.setText(imageInfo.get(position).picturecount + context.getString(R.string.photos));
	    return convertView;
	  }
	
	/* class ViewHolder */
	public class ViewHolder {
		TextView name;
	    TextView picturecount;
	    ImageView icon;
	}
}