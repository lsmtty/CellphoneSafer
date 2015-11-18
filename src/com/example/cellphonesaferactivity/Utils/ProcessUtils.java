package com.example.cellphonesaferactivity.Utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.util.Log;

import com.example.cellphonesaferactivity.Beans.ProcessInfo;

public class ProcessUtils 
{
	public static ArrayList<ProcessInfo> getProcessList(Context context) {
		PackageManager pm = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> process = am.getRunningAppProcesses();
		ArrayList<ProcessInfo> processList = new ArrayList<ProcessInfo>();
		for (RunningAppProcessInfo processSingle : process) {
			int pid = processSingle.pid;  			//获取进程的pid码
			String packageName = processSingle.processName;
			PackageInfo packageInfo = null;
			try {
				packageInfo = pm.getPackageInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}   //通过包管理器获得图标和应用名
			Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
			String apkName = (String) packageInfo.applicationInfo.loadLabel(pm);
			boolean isUserApp = false;
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0)
			{
				//是1是系统应用
				isUserApp = !isUserApp;
			}
			MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
			
			//davikHeap的值是K为单位，如果想用Formatter必须要变为bit为单位，即把int变为long
			ProcessInfo processInfo = new ProcessInfo(icon, packageName, apkName, processMemoryInfo[0].dalvikPrivateDirty*1024, isUserApp);
			processList.add(processInfo);
		}
		return processList;
	}
	
	/**
	 * 
	 */
	public static void killAllProcess(Context context) 
	{
		PackageManager pm = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> process = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : process) {
			String packageName = runningAppProcessInfo.processName;
			am.killBackgroundProcesses(packageName);
		}
	}
}
