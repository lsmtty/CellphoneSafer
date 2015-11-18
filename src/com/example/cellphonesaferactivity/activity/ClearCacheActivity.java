package com.example.cellphonesaferactivity.activity;

import java.lang.reflect.Method;
import java.util.List;

import com.example.cellphonesafer.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * 
 * @author 思敏
 *
 */
@Deprecated
public class ClearCacheActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_cache_layout);
		initUI();
		int i= 3;
	}

	private void initUI() {
		// TODO Auto-generated method stub
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
	
		for (PackageInfo packageInfo : installedPackages) {
			getCacheSize(packageInfo);
		}
	}
	
	private void getCacheSize(PackageInfo packageInfo) {
		try {
			Class<?> clazz = getClassLoader().loadClass("packageManager");
			//通过反射获取到当前的方法
			Method method = clazz.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
