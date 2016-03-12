package com.iStudy.Study.Gallery;

import com.iStudy.Study.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
	Context  context;
	String[] rowid;
	String album;
	public GridViewAdapter(Context activity, String name, String[] rows) {
		context = activity;
		rowid = rows;
		album = name;
	}
	
	public int getCount() {
		return rowid.length;
	}
	
	public Object getItem(int position) {
		return rowid[position];
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.grid_gallery, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageItem);
			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}
		imageView.setImageBitmap(Thumbnails.getThumbnail(context.getContentResolver(), Integer.valueOf(rowid[position]), Thumbnails.MICRO_KIND, new BitmapFactory.Options()));
		return convertView;
	}
}