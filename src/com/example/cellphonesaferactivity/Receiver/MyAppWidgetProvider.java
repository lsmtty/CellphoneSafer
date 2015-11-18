package com.example.cellphonesaferactivity.Receiver;

import com.example.cellphonesaferactivity.service.WidgetRefresh;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 创建桌面小部件的步骤：
 * 1.需要在清单文件中配置原数据
 * 2.需要配置当前元数据中的xml
 * 3.配置一个广播接收者
 * 4.实现界面小部件的layout
 * @author 思敏
 *
 */
public class MyAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

	}
	/**
	 * 第一次创建的时候会调用，并且绝对不要把耗时操作放在receiver中，因为其生命时常一般只有10秒
	 * @param context
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Intent intent = new Intent(context,WidgetRefresh.class);
		context.startService(intent);
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context,WidgetRefresh.class);
		context.stopService(intent);
		}
}
