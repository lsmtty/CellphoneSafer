package com.example.cellphonesaferactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

public class MobileProtectSetting3 extends GestureActivity {
	private Button buttonNext;
	private Button buttonLast;
	private EditText safenumber;
	private Button getContactorNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safersetting3);
		init();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
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
		getContactorNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(MobileProtectSetting3.this,
						ChooseContactor.class), 100);
			}
		});
	}

	private void init() {
		buttonNext = (Button) findViewById(R.id.next);
		buttonLast = (Button) findViewById(R.id.last);
		getContactorNumber = (Button) findViewById(R.id.bt_choose_contactor);
		safenumber = (EditText) findViewById(R.id.safe_number);
		String number = SharedPreferencesUtils.getString("config",
				getApplicationContext(), "safe_number", null);
		if (number != null)
			safenumber.setText(number);
	}

	public void toNext() {
		if (TextUtils.isEmpty(safenumber.getText().toString())) {
			// 未填写号码不能进入下一页
			Toast.makeText(MobileProtectSetting3.this, "您未选择安全号码",
					Toast.LENGTH_LONG).show();
		} else {
			// 记录填写号码，并进入下一页
			SharedPreferencesUtils.setString("config", getApplicationContext(),
					"safe_number", safenumber.getText().toString());
			startActivity(new Intent(MobileProtectSetting3.this,
					MobileProtectSetting4.class));
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			finish();
		}
	}

	public void toPrevious() {
		startActivity(new Intent(MobileProtectSetting3.this,
				MobileProtectSetting2.class));
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			Bundle bundle = data.getExtras();
			safenumber.setText(bundle.getString("phone").replaceAll("-", ""));
		}
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
