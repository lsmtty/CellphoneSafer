package com.example.cellphonesaferactivity.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Receiver.AdminReceiver;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

public class MobileProtectSetting4 extends GestureActivity
{
	protected static final CharSequence OPEN_PROTECT = "已开启手机防盗";
	protected static final CharSequence CLOSE_PROTECT = "未开启手机防盗";
	private Button buttonFinish;
	private Button buttonLast;
	private CheckBox cb;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safersetting4);
		
		init();
	}

	private void init() {
		mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE); //获取设备策略服务
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class); //设备管理组件
		buttonFinish = (Button) findViewById(R.id.next);
		buttonLast = (Button) findViewById(R.id.last);
		cb = (CheckBox) findViewById(R.id.cb_startprotect);
		boolean steal_protect = SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "steal_protect", false);
		cb.setChecked(steal_protect);
		if(steal_protect)
		{
			cb.setText(OPEN_PROTECT);
		}
		else {
			cb.setText(CLOSE_PROTECT);
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				cb.setChecked(isChecked);
				SharedPreferencesUtils.setBoolean("config", getApplicationContext(), "steal_protect", isChecked);
				if(isChecked)
				{
					cb.setText(OPEN_PROTECT);
					activeAdmin();			  //激活设备管理器
				}
				else {
					unInstall();
					cb.setText(CLOSE_PROTECT);//取消设备管理器
				}
			}
		});
		buttonFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//进入手机防盗主页
				//startActivity(new Intent(MobileProtectSetting4.this,MobileProtectSetting3.class));
				toNext();
			}
		});
		buttonLast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toPrevious();
			}
		});
	}
	
	public void toNext() {
		startActivity(new Intent(MobileProtectSetting4.this,MobileProtectSettingFinish.class));
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		finish();
	}
	
	public void toPrevious()
	{
		startActivity(new Intent(MobileProtectSetting4.this,MobileProtectSetting3.class));
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
		finish();
	}

	@Override
	public void moveToRight() {
		// TODO Auto-generated method stub
		toNext();
	}

	@Override
	public void moveToLeft() {
		// TODO Auto-generated method stub
		toPrevious();
	}
	//激活设备管理器,代码实现，也可以通过手动实现
	public void activeAdmin()
	{
		Log.i("message", "activeAdmin");
		if(!mDPM.isAdminActive(mDeviceAdminSample))
		{
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "比系统都安全的设备管理器");
			startActivity(intent);
		}
	}
	
	//取消激活设备管理器
	public void unInstall() {
		if(mDPM.isAdminActive(mDeviceAdminSample)){
			mDPM.removeActiveAdmin(mDeviceAdminSample);//取消激活
		}
	}
}
