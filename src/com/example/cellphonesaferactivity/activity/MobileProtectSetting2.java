package com.example.cellphonesaferactivity.activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.MyJoB.MyTextView;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
public class MobileProtectSetting2 extends GestureActivity 
{
	private Button buttonNext;
	private Button buttonLast;
	private MyTextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safersetting2);
		init();
	}

	private void init() {
		tv = (MyTextView) findViewById(R.id.mv_bind);
		tv.setChecked(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "bind_cellphone", false));
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("message", ""+tv.getChecked());
				tv.setChecked(!tv.getChecked());
				//tv的Checked状态已经更改了，那么下一句直接获取到的就是最新的状态信息
				SharedPreferencesUtils.setBoolean("config", getApplicationContext(), "bind_cellphone", tv.getChecked());
				if (tv.getChecked()) {//当为true时记录Sim卡序列号
					TelephonyManager tm = (TelephonyManager) MobileProtectSetting2.this.getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					Log.i("sim卡序列号", simSerialNumber);
					SharedPreferencesUtils.setString("config", getApplicationContext(), "sim", simSerialNumber);
				}
				else {
					SharedPreferencesUtils.setString("config", getApplicationContext(), "sim", "");
				}
			}
		});
		buttonNext = (Button) findViewById(R.id.next);
		buttonLast = (Button) findViewById(R.id.last);
		buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		if(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "bind_cellphone", false))
		{
			startActivity(new Intent(MobileProtectSetting2.this,MobileProtectSetting3.class));
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			finish();
		}
		else {
			Toast.makeText(MobileProtectSetting2.this, "您未开启SIM卡绑定", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void toPrevious() {
		startActivity(new Intent(MobileProtectSetting2.this,MobileProtectSetting1.class));
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
}
