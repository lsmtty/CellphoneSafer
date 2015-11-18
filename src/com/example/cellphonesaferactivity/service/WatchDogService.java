package com.example.cellphonesaferactivity.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.cellphonesaferactivity.activity.WatchDogActivity;
import com.example.cellphonesaferactivity.db.MySoftwareDbHelper;

public class WatchDogService extends Service 
{
	private static String SOFTURI  = "content://lsmtty.example.softprovider/software";
	private SoftStartReceiver softStartReceiver;
	private ActivityManager am;
	private ArrayList<String> softItem;
	private ArrayList<String> lockSoftware;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			refreshData();
		};
	};
	private MyContentObserver myContentObserver;
	private ContentResolver contentResolver;
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		am = (ActivityManager) WatchDogService.this.getSystemService(Context.ACTIVITY_SERVICE);
		softItem = new ArrayList<String>();
		lockSoftware = new ArrayList<String>();
		myContentObserver = new MyContentObserver(mHandler);
		contentResolver = getContentResolver();
		initData();
		
	}
	private void initData() {
		// TODO Auto-generated method stub
		//注册内容观察者
		contentResolver.registerContentObserver(Uri.parse(SOFTURI), true, myContentObserver);
		refreshData();
		
	}
	/**
	 * 刷新列表
	 */
	private void refreshData() {
		// TODO Auto-generated method stub
		softItem.clear();
		Cursor softwareLocks = null;
		try {
			softwareLocks = contentResolver.query(Uri.parse(SOFTURI),null, null, null,null);
			while(softwareLocks.moveToNext())
			{
				Log.i("message", softwareLocks.getString(1));
				softItem.add(softwareLocks.getString(1));
			}
		} 
		finally{
			softwareLocks.close();
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		softStartReceiver = new SoftStartReceiver();
		registerReceiver(softStartReceiver,new IntentFilter("com.example.cellphone.watchDog"));
		registerReceiver(softStartReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true)
				{
					
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for (RunningTaskInfo taskInfo : runningTasks) {
						String packageName = taskInfo.baseActivity.getPackageName();
						Log.i("message", packageName);
						if(softItem.contains(packageName) && !lockSoftware.contains(packageName))
						{
							Intent watchDogIntent = new Intent(WatchDogService.this,WatchDogActivity.class);
							watchDogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							watchDogIntent.putExtra("name", packageName);
							startActivity(watchDogIntent);
						}
					}
					SystemClock.sleep(4000);
				}	
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	class SoftStartReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if("com.example.cellphone.watchDog".equals(intent.getAction()))
			{
				//设置解密成功，不再弹出窗口
				lockSoftware.add(intent.getStringExtra("name"));
			}
			else {
				lockSoftware.clear();
			}
		}
	}
	
	/**
	 * 自定义观察者
	 * @author 思敏
	 *
	 */
	class MyContentObserver extends ContentObserver
	{
		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(1);
			Log.i("message", "dataChange");
			super.onChange(selfChange);
		}
	}
}
