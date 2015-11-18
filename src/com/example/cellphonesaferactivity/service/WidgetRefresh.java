package com.example.cellphonesaferactivity.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Receiver.MyAppWidgetProvider;
import com.example.cellphonesaferactivity.Utils.ProcessUtils;
import com.example.cellphonesaferactivity.Utils.SystemInfoUtils;

public class WidgetRefresh extends Service {

	private AppWidgetManager widgetManager;
	private Timer timer;
	private TimerTask timerTask;
	private ComponentName provider;
	private MyClickReceiver myClickReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		widgetManager = AppWidgetManager.getInstance(getApplicationContext()); //用桌面小控件管理器管理，通过getInstance获得
		timer = new Timer();
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refresh();
			}
		};
		timer.schedule(timerTask, 1000,5000);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.cellphoneSafer");
		myClickReceiver = new MyClickReceiver();
		registerReceiver(myClickReceiver, intentFilter);
		
	}
	
	private void refresh()
	{
		RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);
		//注意用RemoteViews如何和填充内部的其他控件，使用特别的findview
		int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());
		views.setTextViewText(R.id.tv_up, "正在运行的软件:"+processCount);
		//获取可用内存
		views.setTextViewText(R.id.tv_down, "可用内存:"+Formatter.formatFileSize(getApplicationContext(), SystemInfoUtils.getAvailMem(getApplicationContext())));
		Intent intent = new Intent();
		intent.setAction("com.example.cellphoneSafer");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
		views.setOnClickPendingIntent(R.id.bt_clear, pendingIntent);
		provider = new ComponentName(getApplicationContext(), MyAppWidgetProvider.class);
		//更新桌面
		widgetManager.updateAppWidget(provider, views);
	}
	/**
	 * 接收到点击广播应该清除所有线程
	 * @author 思敏
	 *
	 */
	class MyClickReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ProcessUtils.killAllProcess(getApplicationContext());
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myClickReceiver);  //销毁时不要忘记解绑
		if(timer!=null&&timerTask!=null)
		{
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
	}
}
