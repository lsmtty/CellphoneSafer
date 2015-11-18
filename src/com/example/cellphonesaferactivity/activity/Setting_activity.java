package com.example.cellphonesaferactivity.activity;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.MyJoB.MyChooseTextview;
import com.example.cellphonesaferactivity.MyJoB.MyTextView;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
import com.example.cellphonesaferactivity.service.WatchDogService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Setting_activity extends Activity 
{
	private MyTextView update;
	private MyTextView showPhoneLocation;
	private MyChooseTextview chooseLocationStyle;
	private String [] colorStyle = new String []{"缤纷橙","天空蓝","低调灰","璀璨绿","水晶白"};
	private String [] refuseStyle = new String []{"回复用户繁忙","回复已停机"};
	private AlertDialog style;
	private Button moveToast;
	private MyChooseTextview chooseRefuseMeg;
	private MyTextView softwarelock;
	private Intent softService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		init();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		chooseLocationStyle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder setStyle = new Builder(Setting_activity.this);
				setStyle.setIcon(getResources().getDrawable(R.drawable.abc_ab_bottom_solid_light_holo));
				setStyle.setTitle("归属地显示风格");
				RelativeLayout ll = (RelativeLayout) RelativeLayout.inflate(Setting_activity.this, R.layout.listview_layout, null);
				ListView lv = (ListView) ll.findViewById(R.id.inner_listview);
				lv.setAdapter(new ArrayAdapter<String>(Setting_activity.this, R.layout.small_list_view, R.id.inner_tv,colorStyle));
				lv.setOnItemClickListener(new MyItemClickListener1());
				setStyle.setView(ll);
				style = setStyle.show();
			}
		});
		chooseRefuseMeg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder setStyle = new Builder(Setting_activity.this);
				setStyle.setIcon(getResources().getDrawable(R.drawable.abc_ab_bottom_solid_light_holo));
				setStyle.setTitle("拒接回复设置");
				RelativeLayout ll = (RelativeLayout) RelativeLayout.inflate(Setting_activity.this, R.layout.listview_layout, null);
				ListView lv = (ListView) ll.findViewById(R.id.inner_listview);
				lv.setAdapter(new ArrayAdapter<String>(Setting_activity.this, R.layout.small_list_view, R.id.inner_tv,refuseStyle));
				lv.setOnItemClickListener(new MyItemClickListener2());
				setStyle.setView(ll);
				style = setStyle.show();
			}
		});
		moveToast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Setting_activity.this,ChooseToastActivity.class));
			}
		});
	}

	private void init() {
		update = (MyTextView) findViewById(R.id.setting_update);
		showPhoneLocation = (MyTextView) findViewById(R.id.setting_showPhoneLocation); 
		softwarelock = (MyTextView) findViewById(R.id.setting_software_lock);
		//默认未开启
		update.setChecked(SharedPreferencesUtils.getBoolean("config",getApplicationContext(), "auto_update", false));
		showPhoneLocation.setChecked(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "phone_location", false));
		
		boolean issoftlock = SharedPreferencesUtils.getBoolean("config",getApplicationContext(), "software_lock", false);
		softwarelock.setChecked(issoftlock);
		//如果程序锁开启，启动程序服务
		if(issoftlock)
		{
			startSoftService();
		}
		chooseLocationStyle = (MyChooseTextview) findViewById(R.id.setting_chooseStyle);
		chooseLocationStyle.setMsg(colorStyle[SharedPreferencesUtils.getInt("config", getApplicationContext(), "style", 0)]);
		chooseRefuseMeg = (MyChooseTextview) findViewById(R.id.setting_chooseRefuseStyle);
		chooseRefuseMeg.setMsg(refuseStyle[SharedPreferencesUtils.getInt("config", getApplicationContext(), "refuse", 0)]);
		moveToast = (Button) findViewById(R.id.bt_move);
		
	}
	public void click(View v) 
	{
		switch (v.getId()) {
		case R.id.setting_update:
			update.setChecked(!update.getChecked());
			SharedPreferencesUtils.setBoolean("config",getApplicationContext(), "auto_update", update.getChecked());
			break;
		case R.id.setting_showPhoneLocation:
			showPhoneLocation.setChecked(!showPhoneLocation.getChecked());
			SharedPreferencesUtils.setBoolean("config", getApplicationContext(), "phone_location", showPhoneLocation.getChecked());
			break;
		case R.id.setting_software_lock:
			softwarelock.setChecked(!softwarelock.getChecked());
			if(softwarelock.getChecked())
			{
				startSoftService();
			}
			SharedPreferencesUtils.setBoolean("config", getApplicationContext(), "software_lock", softwarelock.getChecked());
			break;
		default:
			break;
		}
	}
	
	private void startSoftService()
	{
		softService = new Intent(Setting_activity.this,WatchDogService.class);
		startService(softService);
	}
	class MyItemClickListener1 implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SharedPreferencesUtils.setInt("config", getApplicationContext(), "style", position);
			chooseLocationStyle.setMsg(colorStyle[position]);
			style.dismiss();
		}
	}
	
	class MyItemClickListener2 implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SharedPreferencesUtils.setInt("config", getApplicationContext(), "refuse", position);
			chooseRefuseMeg.setMsg(refuseStyle[position]);
			style.dismiss();
		}
	}
}
