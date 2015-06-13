package com.example.activity;

import com.example.kubimusic.R;
import com.example.util.Util;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BigImageActivity extends ActivityBase{
	private ImageView view;
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bigimage);
		view = (ImageView) findViewById(R.id.big_ImageView);
		btn = (Button) findViewById(R.id.bigimage_btn);
		final String url = getIntent().getStringExtra("image");
		BitmapUtils bitmapUtils = new BitmapUtils(this);
		bitmapUtils.display(view, url, Util.getConfig(this, R.drawable.bibi));
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BigImageActivity.this,LrcActivity.class);
//				intent.putExtra("url", url);
				SharedPreferences preferences = getSharedPreferences("image", Context.MODE_PRIVATE);
				Editor  editor = preferences.edit();
				editor.putString("url", url);
				editor.commit();
				startActivity(intent);
			}
		});
	}

}
