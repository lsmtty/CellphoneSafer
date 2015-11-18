package com.example.cellphonesaferactivity.MyJoB;

import com.example.cellphonesafer.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyChooseTextview extends LinearLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.cellphonesafer";
	private TextView titleText;
	private ImageView choose;
	private TextView msg;
	public MyChooseTextview(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();

	}


	public MyChooseTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();

		String title = attrs.getAttributeValue(NAMESPACE,"tv_choose");
		titleText.setText(title);
	}

	public MyChooseTextview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public void setMsg(String msgString) {
		msg.setText(msgString);
	}
	private void init() {
		// TODO Auto-generated method stub
		View.inflate(getContext(), R.layout.choose_item, this);
		titleText = (TextView) findViewById(R.id.tv_title);
		choose = (ImageView) findViewById(R.id.iv_choose);
		msg = (TextView) findViewById(R.id.text_msg);
	}

}
