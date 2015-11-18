package com.example.cellphonesaferactivity.activity;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.MD5Utils;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Int2;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WatchDogActivity extends Activity implements OnClickListener
{
	private EditText password;
	private Button confirm;
	private String stopPackage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.watchdog_layout);
		stopPackage = getIntent().getStringExtra("name");
		init();
	}

	private void init() {
		password = (EditText) findViewById(R.id.et_password);
		confirm = (Button) findViewById(R.id.bt_confirm);
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(password.getText().toString().isEmpty())
		{
			Toast.makeText(WatchDogActivity.this, "输入为空", Toast.LENGTH_SHORT).show();
		}
		else {
			String md5String = MD5Utils.getMD5String(password.getText().toString());
			if(SharedPreferencesUtils.getString("config", getApplicationContext(), "safer_key", "000").equals(md5String))
			{
				finish();
				Intent intent = new Intent();
				intent.setAction("com.example.cellphone.watchDog");
				intent.putExtra("name", stopPackage);
				sendBroadcast(intent);
				//发送报告到service 说明登录成功，不需要再监督了
			}
			else {
				Toast.makeText(WatchDogActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}
}
