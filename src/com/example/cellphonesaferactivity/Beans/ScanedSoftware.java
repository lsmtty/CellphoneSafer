package com.example.cellphonesaferactivity.Beans;

public class ScanedSoftware {
	private String appName;
	private String packageName;
	boolean isVirus ;
	public ScanedSoftware(String appName, String packageName, boolean isVirus) {
		super();
		this.appName = appName;
		this.packageName = packageName;
		this.isVirus = isVirus;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isVirus() {
		return isVirus;
	}
	public void setVirus(boolean isVirus) {
		this.isVirus = isVirus;
	}
	@Override
	public String toString() {
		return "ScanedSoftware [appName=" + appName + ", packageName="
				+ packageName + ", isVirus=" + isVirus + "]";
	}
	
}
