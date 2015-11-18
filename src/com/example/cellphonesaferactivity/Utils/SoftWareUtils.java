package com.example.cellphonesaferactivity.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.cellphonesaferactivity.Beans.AppInfo;

public class SoftWareUtils 
{
	/**
	 * 获取所有安装应用的列表
	 * @param context
	 * @return
	 */
	public static ArrayList<AppInfo> getSoftWareList(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		for (PackageInfo packageInfo : installedPackages) {
			//获取到应用程序的图标
			Drawable loadIcon = packageInfo.applicationInfo.loadIcon(pm);
			//获取到应用名称
			String loadLabel = packageInfo.applicationInfo.loadLabel(pm).toString();
			//获取到包名
			String packageName = packageInfo.packageName;
			//获取到apk资源的路径
			String sourceDir = packageInfo.applicationInfo.sourceDir;			
			File file  = new File(sourceDir);
			//获取apk的大小
			long size = file.length();
			boolean isUserApp = false;
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0)
			{
				//是1是系统应用
				isUserApp = !isUserApp;
			}
			boolean isRom = true;
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0)
			{
				isRom  = false;
			}
			AppInfo appInfo = new AppInfo(loadLabel, loadIcon, size, isUserApp, isRom, packageName,sourceDir);
			appList.add(appInfo);	
		
		}
		return appList;
	}
}
