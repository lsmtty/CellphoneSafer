package com.example.cellphonesaferactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.cellphonesafer.R;

public class MobileProtectSetting1 extends GestureActivity 
{
	private Button buttonNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safersetting1);
		init();
	}

	private void init() {
		buttonNext = (Button) findViewById(R.id.next);
		buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toNext();
			}
		});
	}
	public void toNext()
	{
		startActivity(new Intent(MobileProtectSetting1.this,MobileProtectSetting2.class));
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
		
	}
	
}
