package com.example.cellphonesaferactivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.cellphonesafer.R;
public class AdvancedToolsActivity extends Activity 
{
	private Button phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_tools);
		init();
	}

	private void init() {
		phone = (Button) findViewById(R.id.bt_serachphone);
		phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AdvancedToolsActivity.this,PhoneLocationActivity.class));
			}
		});
	}
}
