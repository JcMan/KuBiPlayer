package com.example.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.kubimusic.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.example.adapter.*;

public class DownloadImageActivity extends ActivityBase implements OnClickListener{
	private TextView back;
	private EditText text;
	private Button btn;
	private MyGridViewAdapter adapter;
	private GridView mGridView;
	private List<String> list;
	String picURL = "http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn=1&word=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloadmage);
		back = (TextView) findViewById(R.id.downimage_back);
		text = (EditText) findViewById(R.id.downimage_edit);
		btn = (Button) findViewById(R.id.downImage_btn_search);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = (String) mGridView.getItemAtPosition(position);
				Intent intent   = new Intent(DownloadImageActivity.this,BigImageActivity.class); 
				intent.putExtra("image", url);
				startActivity(intent);
				
			}
			
		});
		back.setOnClickListener(this);
		btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.downimage_back){
			finish();
		}else if(v.getId()==R.id.downImage_btn_search){
			searchImages();
		}
		
	}
	/**
	 * ËÑË÷Ð´Õæ
	 */
	private void searchImages() {
		String name = text.getText().toString();
		try{
			picURL+=URLEncoder.encode(name, "utf-8");
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configCurrentHttpCacheExpiry(1000*5);
			httpUtils.send(HttpMethod.GET, picURL, new RequestCallBack<String>(){

				@Override
				public void onFailure(HttpException arg0, String arg1){
					Log.e("Second", "Failure");
				}
				@Override
				public void onSuccess(ResponseInfo<String> arg0){
					Log.e("Second", "Success");
					try {
						JSONObject object = new JSONObject(arg0.result);
						JSONArray mArray = object.getJSONArray("data");
						if(mArray.length()>1){
							list = new ArrayList<String>();
							for(int i=0;i<mArray.length()-1;i++){
								JSONObject oneObject = mArray.getJSONObject(i);
								String url = oneObject.getString("objURL");
								list.add(url);
								if(i==mArray.length()-2){
									adapter = new MyGridViewAdapter(DownloadImageActivity.this, list);
									mGridView.setAdapter(adapter);
									break;
								}
							}
						}
					} catch (JSONException e) {}
				}
			});
		} catch (UnsupportedEncodingException e) {}
		
	}

}
