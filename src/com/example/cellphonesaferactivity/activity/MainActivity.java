package com.example.cellphonesaferactivity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.MD5Utils;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int[] PhotoId = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings

	};
	private String[] photoName = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initList();
		gridView = (GridView) findViewById(R.id.main_gv);
		gridView.setAdapter(new SimpleAdapter(this, list,
				R.layout.main_grid_item, new String[] { "id", "name" },
				new int[] { R.id.griditem_iv, R.id.griditem_tv }));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			private RelativeLayout setPasswordLayout;
			private AlertDialog setPasswordDialog;
			private EditText password;
			private EditText confirm_password;
			private RelativeLayout inputPasswordLayout;
			private EditText input_password;
			private AlertDialog inputDialog;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Log.i("message", "" + position);
				switch (position) {
				case 0:
					if ("0".equals(SharedPreferencesUtils.getString("config",
							MainActivity.this, "safer_key", "0"))) 
					{
						AlertDialog.Builder setPassword = new Builder(
								MainActivity.this);
						setPassword.setIcon(getResources().getDrawable(
								R.drawable.abc_ab_bottom_solid_light_holo));
						setPassword.setTitle("设置安全密码");
						setPasswordLayout = (RelativeLayout) getLayoutInflater()
								.inflate(R.layout.setpassword_layout, null); // 动态填充，防止反复调用会显示已经有父布局
						password = (EditText) setPasswordLayout
								.findViewById(R.id.input_password);
						confirm_password = (EditText) setPasswordLayout
								.findViewById(R.id.confim_password);
						Button positive = (Button) setPasswordLayout
								.findViewById(R.id.positive);
						Button negative = (Button) setPasswordLayout
								.findViewById(R.id.negative);
						positive.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(TextUtils.isEmpty(password.getText().toString())||TextUtils.isEmpty(confirm_password.getText().toString()))
								{
									Toast.makeText(MainActivity.this, "密码不能为空", 1).show();
								}
								else {
									if(!password.getText().toString().equals(confirm_password.getText().toString()))
									{
										Toast.makeText(MainActivity.this, "两次输入密码不相同", 1).show();
									}
									else {
										String MD5Password = MD5Utils.getMD5String(password.getText().toString());
										Log.i("password", MD5Password);
										SharedPreferencesUtils.setString("config",MainActivity.this, "safer_key", MD5Password);
										Toast.makeText(MainActivity.this, "密码保存成功", 1).show();
										startActivity(new Intent(MainActivity.this,MobileProtectSetting1.class));//保存成功直接进入设置页面
										setPasswordDialog.dismiss();
									}
								}			
							}
						});
						negative.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
									setPasswordDialog.dismiss();
							}
						});
						setPassword.setView(setPasswordLayout);
						setPasswordDialog = setPassword.show();
					} else {//第二次登录不用进入向导页面直接转入手机安全主页
						AlertDialog.Builder inputPassword = new Builder(MainActivity.this);
						inputPassword.setIcon(getResources().getDrawable(
								R.drawable.abc_ab_bottom_solid_light_holo));
						inputPassword.setTitle("请输入安全密码");
						inputPasswordLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.inputpassword_layout, null);
						input_password = (EditText) inputPasswordLayout
								.findViewById(R.id.input_password_enter);
						Button positive_enter = (Button) inputPasswordLayout
								.findViewById(R.id.positive_enter);
						Button negative_enter = (Button) inputPasswordLayout
								.findViewById(R.id.negative_enter);
						inputPassword.setView(inputPasswordLayout);
						positive_enter.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String MD5input = MD5Utils.getMD5String(input_password.getText().toString());
								if(MD5input.equals(SharedPreferencesUtils.getString("config", MainActivity.this, "safer_key", "0")))
								{
									inputDialog.dismiss();
									startActivity(new Intent(MainActivity.this,MobileProtectSettingFinish.class));
									//Toast.makeText(MainActivity.this, "密码正确", 1).show();
									//进入手机防盗页面或者防盗设置页面
								}
								else {
									Toast.makeText(MainActivity.this, "密码错误", 1).show();
									//判断是否有设置安全信息，未设置则首先进入设置页面。已设置则跳过先导页面直接进入主页面
									
								}
							}
						});
						negative_enter.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								inputDialog.dismiss();
							}
						});
						inputDialog = inputPassword.show();
					}
					break;
				case 1:	//通讯卫士页面
					startActivity(new Intent(MainActivity.this,BlackNameActivity.class));
					break;
				case 2:	//软件管理页面
					startActivity(new Intent(MainActivity.this,SoftManagementActivity.class));
					break;
				case 3: //进程管理页面
					startActivity(new Intent(MainActivity.this,ProgressControlActivity.class));
					break;
				case 5://进入手机杀毒页面
					startActivity(new Intent(MainActivity.this,KillVirus.class));
					break;
				case 6://进入缓存清理页面
					startActivity(new Intent(MainActivity.this,ClearCacheActivity.class));
					break;
				case 7: //高级工具页面
					startActivity(new Intent(MainActivity.this,AdvancedToolsActivity.class));
					break;
				case 8: //设置中心页面
					startActivity(new Intent(MainActivity.this,
							Setting_activity.class));
					break;
				default:
					break;
				}
			}
		});
	}

	private void initList() {
		// TODO Auto-generated method stub
		for (int i = 0; i < photoName.length; i++) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("id", PhotoId[i]);
			newMap.put("name", photoName[i]);
			list.add(newMap);
		}
	}
}
