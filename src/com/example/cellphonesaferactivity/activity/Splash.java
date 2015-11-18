package com.example.cellphonesaferactivity.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class Splash extends Activity 
{
	private final static int MSG_UPDATE = 1;
	private final static int MSG_TOAST = 2;
	private static Handler splash_handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE:
				Bundle bundle = (Bundle) msg.obj;
				showUpdateDialog(bundle.getInt("versionCode"),bundle.getString("versionMessage"));
				break;
			case MSG_TOAST:
				//主activity 弹Toast消息
				Toast.makeText(activity, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	private PackageInfo packageInfo;
	private TextView version;
	private Integer versionCodeServer = null;
	private String versionNameServer = null;
	private String versionMessage;
	static private Activity activity;  //注意这里要用静态activity好，用context某些函数没有实现 ，比如finish()
	static private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		ImageView iv = (ImageView) Splash.this.findViewById(R.id.splash_iv);
		iv.setBackgroundResource(R.drawable.luncher_bg);
		version = (TextView) findViewById(R.id.splash_version);
		version.setText("版本号:"+getNativeVersionName());
		progressBar = (ProgressBar) findViewById(R.id.splash_progress);
		activity = this;
		checkAndloadData();
		//检查自动更新是否开启，这里默认不会自动更新
		if(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "auto_update", false))
		{
			checkVersion();
		}
		else {
			new Thread(){
				public void run() {
					SystemClock.sleep(2500); //不自动更新就多显示会儿广告吧~~~
					runOnUiThread(new Runnable() {  //runOnUiThread可以让系统刷新UI
						public void run() {
							startMain();
						}
					});
				};
			}.start();
		}
	}

	/**
	 * 显示提示用户更新选项框
	 * @param versionCode
	 * @param versionMessage
	 */
	static private void showUpdateDialog(int versionCode ,String versionMessage) {
		// TODO Auto-generated method stub
		AlertDialog.Builder logBuilder = new Builder(activity);
		logBuilder.setTitle("有新版本更新亲");
		logBuilder.setMessage("版本:"+versionCode+"    "+versionMessage);
		logBuilder.setIcon(activity.getResources().getDrawable(R.drawable.abc_ab_bottom_solid_dark_holo));
		logBuilder.setCancelable(false);
		logBuilder.setPositiveButton("现在更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//设置更新进度条，并从服务器下载最新版本
				if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
				{
					Toast.makeText(activity, "请检查SD卡状态", Toast.LENGTH_LONG).show();
					startMain();
					return;
				}
				final String url = "http://192.168.23.1:8080/cellphoneSafer/CellphoneSafer.apk";   //下载路径和文件区分大小写
				final String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
				new Thread()
				{
					public void run() {
						
						HttpUtils http = new HttpUtils();
						http.download(url, sdPath+"/CellphoneSafer.apk",true,true, new RequestCallBack<File>() {
							
							public void onStart() {
								progressBar.setVisibility(ProgressBar.VISIBLE);
							};
							public void onLoading(long total, long current, boolean isUploading) {
								progressBar.setMax(100);
								progressBar.setProgress((int)(current/total)*100);
							};
							@Override
							public void onSuccess(
									ResponseInfo<File> responseInfo) {
								// TODO Auto-generated method stub
								progressBar.setVisibility(ProgressBar.GONE);
								//下载完新版本后开始安装
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
								activity.startActivity(intent);
							}

							@Override
							public void onFailure(HttpException error,  //失败输出错误信息比较重要
									String msg) {  
								// TODO Auto-generated method stub
								if("maybe the file has downloaded completely".equals(msg))  //xUtils 判断已经存在 
								{
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.setDataAndType(Uri.fromFile(new File(sdPath+"/CellphoneSafer.apk")), "application/vnd.android.package-archive");
									activity.startActivity(intent);
								}
								else {
									Log.i("Message",msg);
									Log.i("Message", "download_apk_failure");
									splash_handler.sendMessage(splash_handler.obtainMessage(MSG_TOAST, "下载apk失败"));
									startMain();
								}
								
							}
						});
					};
				}.start();
			}
		});
		logBuilder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startMain();
			}
		});
		logBuilder.show();
	}
	
	/**
	 * 获取本地版本名
	 * @return
	 */
	public String getNativeVersionName() 
	{
		PackageManager packageManager = getPackageManager(); //获得包管理器
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);  //获得包信息 
			Log.i("versionName", packageInfo.versionName);
			Log.i("versionCode", ""+packageInfo.versionCode);
			System.out.println("versionCode" + packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packageInfo.versionName;
	}
	
	/**
	 * 获取本地版本号
	 * @return
	 */
	public Integer getNativeVersionCode() 
	{
		PackageManager packageManager = getPackageManager(); //获得包管理器
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);  //获得包信息 
			Log.i("versionName", packageInfo.versionName);
			Log.i("versionCode", ""+packageInfo.versionCode);
			System.out.println("versionCode" + packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packageInfo.versionCode;
	}
	/**
	 * 检查最新版本是否和本地版本相同
	 */
	public void checkVersion()
	{
		
		final String url = "http://192.168.23.1:8080/cellphoneSafer/version_msg.json";  //这里必须加8080端口号，如果用真机测试，请让真机和Tomcat服务器电脑处于同一局域网段，如192.168.26.**;
		new Thread()
		{
			public void run() {
				HttpUtils http = new HttpUtils();
				http.send(HttpMethod.GET, url, null, new RequestCallBack<String>() {
					//total 总共数据， current 现在传输量
					public void onLoading(long total, long current, boolean isUploading) {
						
					};
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.i("Message", "config_failure:"+arg1);
						
						splash_handler.sendMessage(splash_handler.obtainMessage(MSG_TOAST, "获取版本信息失败"));
						startMain();
					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						Log.i("Message", result);
						try {
							JSONObject resultJson = new JSONObject(result);
							versionCodeServer = resultJson.getInt("versionCode");
							versionMessage = resultJson.getString("message");
							if(versionCodeServer>getNativeVersionCode())
							{
								Message msg = splash_handler.obtainMessage();
								msg.what = MSG_UPDATE;
								Bundle bundle = new Bundle();
								bundle.putInt("versionCode", versionCodeServer);
								bundle.putString("versionMessage", versionMessage);
								msg.obj = bundle;
								splash_handler.sendMessage(msg);
							}
							else {
								//暂停显示两秒广告再进入主页
								new Thread(){
									public void run() {
										SystemClock.sleep(2000);
										runOnUiThread(new Runnable() {  //runOnUiThread可以让系统刷新UI
											public void run() {
												startMain();
											}
										});
									};
								}.start();
							}
							versionNameServer = (String) resultJson.get("versionName");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			};
		}.start();
		
	}
	/**
	 * 后台加载数据
	 */
	private void checkAndloadData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(activity.getFilesDir().getAbsolutePath());
				if(file.exists())
				{
					loadDb();
				}
				else {
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loadDb();
				}
			}
		}).start();
	}
	
	/**
	 * 传数据库到本地
	 */
	private void loadDb()
	{
		InputStream is = null;
		FileOutputStream os = null;
		File file = new File(activity.getFilesDir().getAbsolutePath()+"/address.db");
		if(file.exists())
			return;
		try {
			os = new FileOutputStream(file);
			is = getResources().getAssets().open("address.db");  //从资源文件中载入数据放入files文件中，数据库必须在data/data/下才可以使用
			byte [] bytes = new byte[1024];
			int len = 0;
			while((len=is.read(bytes))!=-1)
			{
				os.write(bytes,0,len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 跳转到主页面
	 */
	static public void startMain()
	{
		createShortCut();
		Intent intent = new Intent(activity,MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	private static void createShortCut() {
		// TODO Auto-generated method stub
		//创建快捷方式的intent
		Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//不允许重复创建
		//shortcutintent.putExtra("duplicate", false);
		//需要现实的名字
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
		//设置快捷方式图标
		Parcelable icon = ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,icon );
		//程序入口intent,带launch的activity
		Intent intent2 = new Intent(activity,Splash.class);
		intent2.addCategory(Intent.CATEGORY_LAUNCHER);
		intent2.setAction(Intent.ACTION_MAIN);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
		activity.sendBroadcast(shortcutintent);  //一定明白这里是向桌面发送了一套广播*****
	}
}
