package com.example.cellphonesaferactivity.Beans;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	private Drawable icon;
	private String pkgName;
	private String apkName;
	private long size;
	private boolean isUser;
	private boolean isChecked = false;
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public boolean isChecked() {
		return isChecked;
	}

	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	public ProcessInfo(Drawable icon, String pkgName, String apkName,
			long size, boolean isUser) {
		super();
		this.icon = icon;
		this.pkgName = pkgName;
		this.apkName = apkName;
		this.size = size;
		this.isUser = isUser;
	}

	@Override
	public String toString() {
		return "ProcessInfo [pkgName=" + pkgName + ", apkName=" + apkName
				+ ", size=" + size + ", isUser=" + isUser + ", isChecked="
				+ isChecked + "]";
	}
	
	
}
