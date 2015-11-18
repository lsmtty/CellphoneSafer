package com.example.cellphonesaferactivity.activity;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Beans.AppInfo;
import com.example.cellphonesaferactivity.Beans.ProcessInfo;
import com.example.cellphonesaferactivity.Utils.ProcessUtils;
import com.example.cellphonesaferactivity.Utils.SystemInfoUtils;
import com.example.cellphonesaferactivity.activity.SoftManagementActivity.MySoftAdapter;
import com.example.cellphonesaferactivity.activity.SoftManagementActivity.MySoftAdapter.Holder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class ProgressControlActivity extends Activity implements
		OnClickListener {
	private TextView tv_left;
	private TextView tv_right;
	private TextView titleBar;
	private ListView lv;
	private ArrayList<ProcessInfo> processAll;
	private ArrayList<ProcessInfo> systemProcess;
	private ArrayList<ProcessInfo> userProcess;
	private MyProcessAdapter myProcessAdapter;
	private Button bt_check_all;
	private Button bt_cancel_all;
	private Button bt_clear;
	private Button bt_setting;
	private ActivityManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_layout);
		init();
		initTitleBar();
		initData();
		initLisener();
	}

	private void init() {
		// TODO Auto-generated method stub
		tv_left = (TextView) findViewById(R.id.tv_rom);
		tv_right = (TextView) findViewById(R.id.tv_sd);
		titleBar = (TextView) findViewById(R.id.tv_titlebar);
		lv = (ListView) findViewById(R.id.lv_software);
		bt_check_all = (Button) findViewById(R.id.bt_check_all);
		bt_cancel_all = (Button) findViewById(R.id.bt_cancel_all);
		bt_clear = (Button) findViewById(R.id.bt_clear);
		bt_setting = (Button) findViewById(R.id.bt_setting);
	}

	private void initTitleBar() {
		// TODO Auto-generated method stub
		refreshTitleBar();

	}

	private void initData() {
		// TODO Auto-generated method stub
		processAll = ProcessUtils.getProcessList(ProgressControlActivity.this);
		systemProcess = new ArrayList<ProcessInfo>();
		userProcess = new ArrayList<ProcessInfo>();
		ProcessInfo processInfo = null; // 这里可以改善，直接把processAll的项填充到其他两个list中即可，不用取出来，这是指针索引
		while (processAll.size() != 0) {
			processInfo = processAll.remove(0);
			if (processInfo.isUser()) {
				userProcess.add(processInfo);
			} else {
				systemProcess.add(processInfo);
			}
		}
		processInfo = null;
		processAll = null;
		myProcessAdapter = new MyProcessAdapter();
		lv.setAdapter(myProcessAdapter);
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
				titleBar.setText(firstVisibleItem < 1 + systemProcess.size() ? "系統进程: ("
						+ systemProcess.size() + ")"
						: "用户进程: (" + userProcess.size() + ")");
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0 || position == (systemProcess.size() + 1))
					return;
				Object itemAtPosition = lv.getItemAtPosition(position);// 根据
																		// getItem来获取
				if (itemAtPosition != null
						&& itemAtPosition instanceof ProcessInfo) {
					ProcessInfo processInfo = (ProcessInfo) itemAtPosition;
					processInfo.setChecked(!processInfo.isChecked());
					CheckBox cb = (CheckBox) view.findViewById(R.id.cb_choose);
					cb.setChecked(!cb.isChecked());
				}
			}
		});
		bt_check_all.setOnClickListener(this);
		bt_cancel_all.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
		bt_setting.setOnClickListener(this);
	}

	class MyProcessAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return systemProcess.size() + userProcess.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position < systemProcess.size() + 1) {
				return (systemProcess.get(position - 1)); // 系统进程列表
			} else {
				return userProcess.get(position - 2 - systemProcess.size());
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
			if (position == 0 || position == (systemProcess.size() + 1)) {
				view = View.inflate(ProgressControlActivity.this,
						R.layout.software_title_layout, null);
				TextView smTextView = (TextView) view
						.findViewById(R.id.tv_smallTitle);
				smTextView.setText(position == 0 ? "系統进程: ("
						+ systemProcess.size() + ")" : "用户进程: ("
						+ (userProcess.size()-1) + ")");   //减去不可见的自己的应用
				return view; // 不设置tag,防止拿到标题的view，其中并没有holder会报空指针
			} else {
				ProcessInfo processInfo = null;
				if (position < systemProcess.size() + 1) {
					processInfo = systemProcess.get(position - 1); // 系统进程列表
				} else {
					processInfo = userProcess.get(position - 2
							- systemProcess.size()); // 用户进程
				}
				if (convertView == null || view.getTag() == null) { // 缓存view存在，且tag不为空
					view = View.inflate(ProgressControlActivity.this,
							R.layout.progress_item_layout, null);
					holder = new Holder(view);
					holder.icon = (ImageView) view.findViewById(R.id.iv_logo);
					holder.name = (TextView) view.findViewById(R.id.tv_name);
					holder.size = (TextView) view.findViewById(R.id.tv_size);
					holder.choose = (CheckBox) view
							.findViewById(R.id.cb_choose);
					view.setTag(holder);
				} else {
					holder = (Holder) view.getTag();
				}
				if (processInfo.getPkgName().equals(getPackageName()))
					view.setVisibility(View.GONE);;
				holder.getIconIv().setBackground(processInfo.getIcon());
				holder.getNameTv().setText(processInfo.getApkName());
				holder.getSizeTv().setText(
						Formatter.formatFileSize(getApplicationContext(),
								processInfo.getSize()));
				holder.getChooseCb().setChecked(processInfo.isChecked()); // 放错位置产生了bug,应该在拿到holder以后才可以设置内容
				return view;
			}
		}

		class Holder {
			View baseview;
			ImageView icon;
			TextView name;
			TextView size;
			CheckBox choose;

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

			public CheckBox getChooseCb() {
				if (choose == null) {
					choose = (CheckBox) baseview.findViewById(R.id.cb_choose);
				}
				return choose;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_check_all:
			// 加入检查config部分，是否更新systemProcess
			for (ProcessInfo processInfo : systemProcess) {
				processInfo.setChecked(true);
			}
			for (ProcessInfo processInfo : userProcess) {
				if (processInfo.getPkgName().equals(getPackageName()))
					continue;
				processInfo.setChecked(true);
			}
			myProcessAdapter.notifyDataSetChanged();
			break;
		case R.id.bt_cancel_all:
			// 加入检查config部分，是否更新systemProcess
			for (ProcessInfo processInfo : systemProcess) {
				processInfo.setChecked(false);
			}
			for (ProcessInfo processInfo : userProcess) {
				if (processInfo.getPkgName().equals(getPackageName()))
					continue;
				processInfo.setChecked(false);
			}
			myProcessAdapter.notifyDataSetChanged();
			break;
		case R.id.bt_clear:
			am = (ActivityManager) ProgressControlActivity.this
					.getSystemService(Context.ACTIVITY_SERVICE);
			long count = 0;
			// 获取系统进程迭代器
			Iterator<ProcessInfo> iterator = systemProcess.iterator();
			ProcessInfo processInfo = null;
			while (iterator.hasNext()) {
				processInfo = iterator.next();
				if (processInfo.isChecked()) {
					count += processInfo.getSize();
					iterator.remove();
					am.killBackgroundProcesses(processInfo.getPkgName());
				}
			}
			// 获取用户进程迭代器
			Iterator<ProcessInfo> iterator2 = userProcess.iterator();
			processInfo = null;
			while (iterator2.hasNext()) {
				processInfo = iterator2.next();
				Log.i("processInfo", processInfo.toString());
				if (processInfo.isChecked()) {
					count += processInfo.getSize();
					iterator2.remove();
					am.killBackgroundProcesses(processInfo.getPkgName());
				}
			}
			Toast.makeText(
					ProgressControlActivity.this,
					"释放了"
							+ Formatter.formatFileSize(
									ProgressControlActivity.this, count)
							+ "的空间", Toast.LENGTH_SHORT).show();
			myProcessAdapter.notifyDataSetChanged();
			refreshTitleBar();
			break;
		case R.id.bt_setting:
			startActivity(new Intent(ProgressControlActivity.this,
					ProgressSetting.class));
			break;
		default:
			break;
		}
	}

	private void refreshTitleBar() {
		// TODO Auto-generated method stub
		tv_left.setText("进程总数:"
				+ SystemInfoUtils.getProcessCount(ProgressControlActivity.this));
		tv_right.setText("剩余/总内存 : "
				+ Formatter.formatFileSize(ProgressControlActivity.this,
						SystemInfoUtils
								.getAvailMem(ProgressControlActivity.this))
				+ "/"
				+ Formatter.formatFileSize(ProgressControlActivity.this,
						SystemInfoUtils
								.getTotalMem(ProgressControlActivity.this)));
	}
}
