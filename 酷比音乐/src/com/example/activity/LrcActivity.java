package com.example.activity;

import com.lidroid.xutils.BitmapUtils;
import com.mylrc.tool.CLrcCtrl;
import com.example.*;
import com.example.kubimusic.MediaPlayerDAL;
import com.example.kubimusic.Music;
import com.example.kubimusic.R;
import com.example.kubimusic.R.id;
import com.example.util.MusicUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class LrcActivity extends ActivityBase implements OnClickListener{
	
	
	private CLrcCtrl mLrcView;
	private String name;
	private TextView mDownLoadLrc,mBack;
	private PopupWindow mPopupWindow;
	private String rootLrcPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/KuBi/lrc/";
	SharedPreferences preferences ;
	Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MediaPlayerDAL.getPlayer().isPlaying()) {
                mLrcView.setSongLength(MediaPlayerDAL.getPlayer().getDuration());
                mLrcView.setcurLength(MediaPlayerDAL.getPlayer().getCurrentPosition());
                mLrcView.invalidate();
            }
            if (mHandler != null){
                mHandler.sendEmptyMessageDelayed(100, 50);
            }
        }
    };
    
    protected void onCreate(android.os.Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_lrc);
    	name = getIntent().getExtras().getString("musicName");
    	initVariable();
    	initView();
    	initListener();
    	bindData();
    	mHandler.sendEmptyMessageDelayed(100, 50);
    }
    
    @Override
    protected void onResume() {
    	
    	String url  = preferences.getString("url","");
    	if(url!=null){
    		BitmapUtils bitmapUtils = new BitmapUtils(this);
    		bitmapUtils.display(findViewById(R.id.lrcactivity_layout), url);
    	}
    	Log.e("67", "resume");
    	super.onResume();
    	
    }
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    	
    	Editor editor = preferences.edit();
    	editor.putString("url", "");
    	editor.commit();
    }

	private void bindData() {
		readLrc(MusicUtil.getRealMusicName(name));
	}

	private void initListener() {
		mDownLoadLrc.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	private void initView() {
		mLrcView = (CLrcCtrl) findViewById(R.id.lrcView);
		mDownLoadLrc = (TextView) findViewById(R.id.tv_download_lrc);
		mBack = (TextView) findViewById(R.id.tv_back);
		mLrcView.SetTextSize(25);
		mLrcView.setTextColor(255, 255, 255);
		mLrcView.setCurrentLineColor(128, 255, 255);
		mLrcView.setMediaPlayer(MediaPlayerDAL.getPlayer());
		mLrcView.setCoding("UTF-8");

		
	}

	private void initVariable() {
		preferences = getSharedPreferences("image", Context.MODE_PRIVATE);
	};
	
	private void readLrc(String name) {
	mLrcView.readlrc(rootLrcPath+name+".lrc");
	mLrcView.setMediaPlayer(MediaPlayerDAL.getPlayer());
	mHandler.sendEmptyMessageDelayed(100, 50);
}

	@Override
	public void onClick(View v){
		if(v.getId()==R.id.tv_download_lrc){
			showPopupWindow();
		}else if(v.getId()==R.id.tv_back){
			finish();
		}else if(v.getId()==R.id.btn_download_lrc){
			startLrcDialogActivity();
			mPopupWindow.dismiss();
		}else if(v.getId()==R.id.btn_download_image){
			startDownloadImageActivity();
			mPopupWindow.dismiss();
		}
		
	}
	/*
	*下载写真的Activity
	*/
	private void startDownloadImageActivity(){
		Intent intent = new Intent(this,DownloadImageActivity.class);
		startActivity(intent);
	}

	/**
	 * 弹出PopupWindow
	 */
	private void showPopupWindow() {
		View view = getLayoutInflater().inflate(R.layout.lrc_popupwinow, null);
		Button btn_download_lrc = (Button) view.findViewById(R.id.btn_download_lrc);
		Button btn_download_image = (Button) view.findViewById(R.id.btn_download_image);
		btn_download_image.setOnClickListener(this);
		btn_download_lrc.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		View parentView = findViewById(R.id.layout_lrc_titlebar);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.animation);
		mPopupWindow.showAsDropDown(parentView,0,0);
		
		
	}

	/**
	 * 转到下载歌词的Activity
	 */
	private void startLrcDialogActivity(){
		Intent _Intent = new Intent(this,LrcDialogActivity.class);
		Bundle bundle = new Bundle();
		Music _Music = MediaPlayerDAL.getMp3List().get(MediaPlayerDAL.getCurrentMusicPosition());
		bundle.putString("MusicName", MusicUtil.getRealMusicName(_Music.name));
		bundle.putString("singer", _Music.singer);
		_Intent.putExtras(bundle);
		startActivityForResult(_Intent,1);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		readLrc(MusicUtil.getRealMusicName(MediaPlayerDAL.getCurrentMusic().name));
	}
}
