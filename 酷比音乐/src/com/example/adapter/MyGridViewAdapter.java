package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.kubimusic.R;
import com.example.util.Util;
import com.lidroid.xutils.BitmapUtils;

public class MyGridViewAdapter extends BaseAdapter{
	private List<String> imageURLs;
	private Context mContext;
	
	public MyGridViewAdapter(Context context,List<String> list){
		imageURLs = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		return imageURLs.size();
	}

	@Override
	public Object getItem(int position){
		return imageURLs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview,null);
		final ImageView view = (ImageView) convertView.findViewById(R.id.iv_imageview);
		BitmapUtils bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.display(view, imageURLs.get(position),Util.getConfig(mContext, R.drawable.bibi));
		return convertView;
	}	
}
