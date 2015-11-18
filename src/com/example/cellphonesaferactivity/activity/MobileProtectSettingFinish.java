package com.example.cellphonesaferactivity.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;


public class MobileProtectSettingFinish extends Activity 
{
	private TextView number;
	private ImageView lock;
	private Button reset;
	private TextView isProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safesetting_finish);
		init();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		number.setText(SharedPreferencesUtils.getString("config", getApplicationContext(), "safe_number", null));
		if(SharedPreferencesUtils.getBoolean("config", getApplicationContext(), "steal_protect", false))
		{
			lock.setImageResource(R.drawable.lock);
			isProtect.setText("防盗保护已开启");
		}
		else {
			lock.setImageResource(R.drawable.unlock);
			isProtect.setText("防盗保护已关闭");
		}
		reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MobileProtectSettingFinish.this,MobileProtectSetting1.class));
				MobileProtectSettingFinish.this.finish();
			}
		});
	}

	private void init() {
		number = (TextView) findViewById(R.id.tv_number);
		lock = (ImageView) findViewById(R.id.img_lock);
		reset = (Button) findViewById(R.id.bt_reset);
		isProtect = (TextView) findViewById(R.id.tv_isProtected);
	}
}
