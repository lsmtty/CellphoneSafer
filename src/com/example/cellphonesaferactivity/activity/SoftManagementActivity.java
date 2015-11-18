package com.example.cellphonesaferactivity.activity;

import java.util.ArrayList;

import org.w3c.dom.ls.LSException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Beans.AppInfo;
import com.example.cellphonesaferactivity.Utils.SoftWareUtils;
import com.example.cellphonesaferactivity.db.MySoftwareDbHelper;
import com.lidroid.xutils.exception.DbException;

public class SoftManagementActivity extends Activity {
	private static String SOFTURI  = "content://lsmtty.example.softprovider/software";
	private TextView tv_rom;
	private TextView tv_sd;
	private ListView lv;
	private TextView titleBar;
	private ArrayList<AppInfo> appAll;
	private ArrayList<AppInfo> systemApp;
	private ArrayList<AppInfo> userApp;
	private MySoftAdapter mySoftAdapter;
	private PopupWindow popupWindow;
	private AppInfo clickAppInfo;
	private Handler clickHandler;
	private ContentResolver contentResolver;
	private Handler softActivityHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				int pos = (Integer) msg.obj;
				if(pos<1+systemApp.size())
				{
					systemApp.remove(pos-1);
				}
				else {
					userApp.remove(pos - 2 - systemApp.size());
				}
				mySoftAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_layout);
		init();
		initTitleBar();
		initData();
		initLisener();

	}

	private void init() {
		tv_rom = (TextView) findViewById(R.id.tv_rom);
		tv_sd = (TextView) findViewById(R.id.tv_sd);
		titleBar = (TextView) findViewById(R.id.tv_titlebar);
		lv = (ListView) findViewById(R.id.lv_software);
		contentResolver = getContentResolver();
	}

	private void initTitleBar() {
		// TODO Auto-generated method stub
		// 获取到rom内存的运行的剩余空间
		long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
		// 获取到sd卡的剩余空间
		long sd_freeSpace = Environment.getExternalStorageDirectory()
				.getFreeSpace();

		tv_rom.setText("内存剩余:" + Formatter.formatFileSize(this, rom_freeSpace));
		tv_sd.setText("sd卡剩余:" + Formatter.formatFileSize(this, sd_freeSpace));
	}

	private void initData() {
		appAll = SoftWareUtils.getSoftWareList(SoftManagementActivity.this);
		systemApp = new ArrayList<AppInfo>();
		userApp = new ArrayList<AppInfo>();
		AppInfo appInfo = null;
		while (appAll.size() != 0) {
			appInfo = appAll.remove(0);
			if (appInfo.isUserApp()) {
				userApp.add(appInfo);
			} else {
				systemApp.add(appInfo);
			}
		}
		appInfo = null;
		appAll = null;
		mySoftAdapter = new MySoftAdapter();
		lv.setAdapter(mySoftAdapter);
	}

	private void initLisener() {
		// TODO Auto-generated method stub
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				titleBar.setText(firstVisibleItem < 1 + systemApp.size() ? "系統应用: ("
						+ systemApp.size() + ")"
						: "用户应用: (" + userApp.size() + ")");
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object itemAtPosition = lv.getItemAtPosition(position);
				if(itemAtPosition!=null && itemAtPosition instanceof AppInfo)
				{
					clickAppInfo = (AppInfo) itemAtPosition;
					String packageName = clickAppInfo.getPackageName();
					if(packageName.equals(getPackageName()))  //本程序 则不处理，向下传递事件
						return false;
					ImageView iv = (ImageView) view. findViewById(R.id.iv_lock);
					Cursor dbCursor = null;
					try {
						dbCursor = contentResolver.query(Uri.parse(SOFTURI),null, "name=?", new String[]{packageName}, null);
						if(dbCursor.moveToLast())
						{
							contentResolver.delete(Uri.parse(SOFTURI),  "name=?", new String[]{packageName});
							iv.setImageDrawable(getResources().getDrawable(R.drawable.unlock));
							
						}
						else {
							ContentValues values  = new ContentValues();
							values.putNull("id");
							values.put("name",packageName);
							contentResolver.insert(Uri.parse(SOFTURI), values);
							iv.setImageDrawable(getResources().getDrawable(R.drawable.lock));
						}
					}
					finally{
						if(dbCursor!=null)
						{
							dbCursor.close();
						}
					}
					
					
				}
				return true;
			}
			
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int localPosition = position;
				// TODO Auto-generated method stub
				if(position==0||position == (systemApp.size() + 1))
					return;
				Object itemAtPosition = lv.getItemAtPosition(position); // 调用adapter的getItem函数
				if (itemAtPosition != null && itemAtPosition instanceof AppInfo) {
					clickAppInfo = (AppInfo) itemAtPosition;
					View contentView = View.inflate(
							SoftManagementActivity.this,
							R.layout.choose_app_popupwindow, null);
					LinearLayout ll_upload = (LinearLayout) contentView
							.findViewById(R.id.ll_upload);
					LinearLayout ll_run = (LinearLayout) contentView
							.findViewById(R.id.ll_run);
					LinearLayout ll_share = (LinearLayout) contentView
							.findViewById(R.id.ll_share);
					/**
					 * 卸载应用
					 */
					ll_upload.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_DELETE);
							if(clickAppInfo.isUserApp())
							{
								intent.setData(Uri.parse("package:"+clickAppInfo.getPackageName()));
								startActivityForResult(intent, 100);
								clickHandler = new Handler()
								{
									public void handleMessage(Message msg) {
										if(msg.what==2)
										{
											softActivityHandler.dispatchMessage(softActivityHandler.obtainMessage(1,localPosition));
										}
									};
								};
							}
							else {//系统应用，需要root权限才可以卸载，通过Linux命令进行卸载
								Toast.makeText(SoftManagementActivity.this, "需要root工具才可卸载", Toast.LENGTH_SHORT).show();
							}
						}
					});
					/**
					 * 运行应用
					 */
					ll_run.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							PackageManager pm = getPackageManager();
							Intent intent = pm.getLaunchIntentForPackage(clickAppInfo.getPackageName());
							if(intent!=null)
							{
								startActivity(intent);
							}
							else {
								//没有界面的应用如法获取启动intent，也就是没有activity
								Toast.makeText(SoftManagementActivity.this, "该应用没有界面", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					/**
					 * 分享应用,一定要学会这个方法
					 */
					ll_share.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, "一定会让你爱不释手的一款应用,名称叫："+clickAppInfo.getApkName()
																+ "下载地址：https://play.google.com/store/apps/details?id="
																+ clickAppInfo.getPackageName());
							startActivity(intent);
						}
					});
					if (popupWindow != null) {
						popupWindow.dismiss();
						popupWindow = null;
					}

					// -2表示wrap_content
					popupWindow = new PopupWindow(contentView, -2, -2);
					// 使用popupwindow必须设置背景，不然不会有动画
					popupWindow.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));

					int[] location = new int[2];
					// 获取view的位置
					view.getLocationInWindow(location);
					popupWindow.showAtLocation(parent, Gravity.LEFT
							+ Gravity.TOP, 70, location[1]);
					ScaleAnimation sa = new ScaleAnimation(0.7f, 1.0f, 0.6f,
							1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					sa.setDuration(600);
					contentView.startAnimation(sa);
				}
			}
		});
	}

	class MySoftAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return systemApp.size() + userApp.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position < systemApp.size() + 1) {
				return (systemApp.get(position - 1)); // 系统进程列表
			} else {
				return userApp.get(position - 2 - systemApp.size());
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			Holder holder = null;
			if (position == 0 || position == (systemApp.size() + 1)) {
				view = View.inflate(SoftManagementActivity.this,
						R.layout.software_title_layout, null);
				TextView smTextView = (TextView) view
						.findViewById(R.id.tv_smallTitle);
				smTextView.setText(position == 0 ? "系統应用: (" + systemApp.size()
						+ ")" : "用户应用: (" + userApp.size() + ")");
				return view; // 不设置tag,防止拿到tag为空报空指针
			} else {
				AppInfo appInfo = null;
				if (position < systemApp.size() + 1) {
					appInfo = systemApp.get(position - 1); // 系统进程列表
				} else {
					appInfo = userApp.get(position - 2 - systemApp.size());
				}
				if (convertView == null || view.getTag() == null) {
					view = View.inflate(SoftManagementActivity.this,
							R.layout.software_item_layout, null);
					holder = new Holder(view);
					holder.icon = (ImageView) view.findViewById(R.id.iv_logo);
					holder.name = (TextView) view.findViewById(R.id.tv_name);
					holder.size = (TextView) view.findViewById(R.id.tv_size);
					holder.lock = (ImageView) view.findViewById(R.id.iv_lock);
					view.setTag(holder);
				} else {
					holder = (Holder) view.getTag();
				}
				holder.getIconIv().setBackground(appInfo.getIcon());
				holder.getNameTv().setText(appInfo.getApkName());
				holder.getSizeTv().setText(
						Formatter.formatFileSize(getApplicationContext(),
								appInfo.getApkSize()));
				//cursor ismovetoFirst 会返回boolean类型，如果为空则返回false
				Cursor queryData = null;  //一定注意游标泄露的问题
				try {
					queryData = contentResolver.query(Uri.parse(SOFTURI),null, "name=?", new String[]{appInfo.getPackageName()}, null);
					if(queryData.moveToFirst())
					{
						holder.getLockIv().setImageDrawable(getResources().getDrawable(R.drawable.lock));
					}
					else {
						holder.getLockIv().setImageDrawable(getResources().getDrawable(R.drawable.unlock));
					}
				}
				finally{
					if(queryData!=null)
						queryData.close();
				}
				return view;
			}
		}

		class Holder {
			View baseview;
			ImageView icon;
			TextView name;
			TextView size;
			ImageView lock;
			public Holder(View v) {
				// TODO Auto-generated constructor stub
				baseview = v;
			}

			public TextView getNameTv() {
				if (name == null) {
					name = (TextView) baseview.findViewById(R.id.tv_name);
				}
				return name;
			}

			public ImageView getIconIv() {
				if (icon == null) {
					icon = (ImageView) baseview.findViewById(R.id.iv_logo);
				}
				return icon;
			}

			public TextView getSizeTv() {
				if (size == null) {
					size = (TextView) baseview.findViewById(R.id.tv_size);
				}
				return size;
			}

			public ImageView getLockIv() {
				if(lock==null)
				{
					lock = (ImageView) baseview.findViewById(R.id.iv_lock);
				}
				return lock;
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if(requestCode==100)
		{
			clickHandler.sendMessage(clickHandler.obtainMessage(2));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
