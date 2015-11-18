package com.example.cellphonesaferactivity.activity;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.MyJoB.MyChooseTextview;
import com.example.cellphonesaferactivity.MyJoB.MyTextView;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
import com.example.cellphonesaferactivity.activity.Setting_activity.MyItemClickListener1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ProgressSetting extends Activity 
{
	private String [] clear_period = new String[]{"30分钟","1小时","2小时","4小时","6小时"};
	private MyTextView showMyTextView;
	private MyChooseTextview choosePeriod;
	private AlertDialog style;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_setting_layout);
		init();
		initData();
		initListener();
	}

	
	private void init() {
		// TODO Auto-generated method stub
		showMyTextView = (MyTextView) findViewById(R.id.setting_showsystem);
		choosePeriod = (MyChooseTextview) findViewById(R.id.choose_clear_time);
	}
	private void initData() {
		// TODO Auto-generated method stub
		showMyTextView.setChecked(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "showSystem", true));
		choosePeriod.setMsg(clear_period[SharedPreferencesUtils.getInt("config", getApplicationContext(), "clear_period", 0)]);
	}
	private void initListener() {
		showMyTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMyTextView.setChecked(!showMyTextView.getChecked());
				SharedPreferencesUtils.setBoolean("config", getApplicationContext(),"showSystem" , showMyTextView.getChecked());
			}
		});
		choosePeriod.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder setStyle = new Builder(ProgressSetting.this);
				setStyle.setIcon(getResources().getDrawable(R.drawable.abc_ab_bottom_solid_light_holo));
				setStyle.setTitle("归属地显示风格");
				RelativeLayout ll = (RelativeLayout) RelativeLayout.inflate(ProgressSetting.this, R.layout.listview_layout, null);
				ListView lv = (ListView) ll.findViewById(R.id.inner_listview);
				lv.setAdapter(new ArrayAdapter<String>(ProgressSetting.this, R.layout.small_list_view, R.id.inner_tv,clear_period));
				lv.setOnItemClickListener(new MyItemClickListener());
				setStyle.setView(ll);
				style = setStyle.show();
			}
		});
	}
	class MyItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			SharedPreferencesUtils.setInt("config", getApplicationContext(), "clear_period", position);
			choosePeriod.setMsg(clear_period[position]);
			style.dismiss();
		}
	}
}
