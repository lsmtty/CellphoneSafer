package com.example.cellphonesaferactivity.activity;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.db.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneLocationActivity extends Activity 
{
	private static final String PHONE_REGRESSION ="^1[34578][0-9]{9}$";
	private EditText phone;
	private TextView location;
	private Button confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serach_phonelocation);
		init();
	}
	/**
	 * 手机震动，需要调用系统服务，并且需要震动权限
	 */
	private void vibrate()
	{
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		//vibrator.vibrate(2000); 震动两秒
		//有规律有模式的震动方式
		vibrator.vibrate(new long[]{1000,2000,1000,3000}, -1);//-1不重复，0到3的值表示从第几位开始重复震动
	}
	private void init() {
		phone = (EditText) findViewById(R.id.et_phone);
		location = (TextView) findViewById(R.id.tv_showlocation);
		confirm = (Button) findViewById(R.id.bt_confirm);
		phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				location.setText(AddressDao.location(s.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(phone.getText().toString().isEmpty())
				{
					//加入抖动效果
					Animation shakeAnimation = AnimationUtils.loadAnimation(PhoneLocationActivity.this, R.anim.shake);
					phone.startAnimation(shakeAnimation);
					vibrate();
					//Toast.makeText(PhoneLocationActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(phone.getText().toString().length()==11)
				{
					if(!phone.getText().toString().matches(PHONE_REGRESSION))
					{
						Toast.makeText(PhoneLocationActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
					}
					else {
						//进行手机归属地查询
						String locationString = AddressDao.location(phone.getText().toString());
						location.setText(locationString);
						//Toast.makeText(PhoneLocationActivity.this, "手机号正确", Toast.LENGTH_SHORT).show();
					}
				}
				else if(phone.getText().toString().length()==5){
					location.setText("客服电话");
				}
				else {
					Toast.makeText(PhoneLocationActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
