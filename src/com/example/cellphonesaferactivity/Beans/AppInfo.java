package com.example.cellphonesaferactivity.Beans;

import android.graphics.drawable.Drawable;

public class AppInfo {
	/**
	 * 程序名字
	 */
	private String apkName;
	/**
	 * 程序图标
	 */
	private Drawable icon;
	
	/**
	 * 程序目录
	 */
	private String path;
	/**
	 * 程序大小
	 */
	private long apkSize;
	public AppInfo(String apkName, Drawable icon, long apkSize,
			boolean userApp, boolean isRom, String packageName,String path) {
		super();
		this.apkName = apkName;
		this.icon = icon;
		this.apkSize = apkSize;
		this.userApp = userApp;
		this.isRom = isRom;
		this.packageName = packageName;
		this.path = path;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public long getApkSize() {
		return apkSize;
	}
	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public boolean isRom() {
		return isRom;
	}
	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * true 用户app
	 * false 系统用户
	 */
	private boolean userApp;
	/**
	 * 存储位置
	 */
	private boolean isRom;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "AppInfo [apkName=" + apkName + ", icon=" + icon + ", path="
				+ path + ", apkSize=" + apkSize + ", userApp=" + userApp
				+ ", isRom=" + isRom + ", packageName=" + packageName + "]";
	}
	/**
	 * 包名
	 */
	private String packageName;
}
