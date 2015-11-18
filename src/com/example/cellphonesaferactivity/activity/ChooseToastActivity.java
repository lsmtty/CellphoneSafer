package com.example.cellphonesaferactivity.activity;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

public class ChooseToastActivity extends Activity 
{
	private ImageView iv;
	private WindowManager mWM;
	private Display display;
	private int winWidth;
	private int winHeight;
	private int startX;
	private int startY;
	private long click[]; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_toast_location);
		init();
		initLisener();
	}

	private void initLisener() {
		// TODO Auto-generated method stub
		//拖拽监听
		iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx = endX - startX;
					int dy = endY - startY;
					iv.setX(iv.getX()+dx);
					iv.setY(iv.getY()+dy);
					if(iv.getX()<0)
					{
						iv.setX(0); 
					}
					if(iv.getY()<0)
					{
						iv.setY(0);
					}
					if(iv.getX()>winWidth-v.getWidth())
					{
						iv.setX(winWidth-v.getWidth()); 
					}
					if(iv.getY()>winHeight-v.getHeight())
					{
						iv.setY(winHeight-v.getHeight()); 
					}
					//重新初始化坐标
					startX = endX;
					startY = endY;
					break;
				case MotionEvent.ACTION_UP:
					SharedPreferencesUtils.setInt("config", getApplicationContext(), "lastX", (int)iv.getX());
					SharedPreferencesUtils.setInt("config", getApplicationContext(), "lastY", (int)iv.getY());
					break;
				default:
					break;
				}
				return false;
			}
		});
		//双击监听（多击监听也相似）
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.arraycopy(click, 1, click, 0, click.length-1);//多次点击都从最后一个位置往前复制
				click[click.length-1] = SystemClock.uptimeMillis();   //uptimeMillis()从系统这次启动的事件开始算起，则最后一个为最后一次点击的事件
				if(click[click.length-1]-click[0]<500)
				{
					//Toast.makeText(ChooseToastActivity.this, "触发了双击操作", Toast.LENGTH_SHORT).show();
					iv.setX(winWidth/2-iv.getWidth()/2);
				}
			}
		});
	}

	private void init() {
		iv = (ImageView) findViewById(R.id.iv_drag);
		iv.setX(SharedPreferencesUtils.getInt("config", getApplicationContext(), "lastX", 0));
		iv.setY(SharedPreferencesUtils.getInt("config", getApplicationContext(), "lastY", 0)-150);
		mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
		display = mWM.getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		winWidth = point.x;
		winHeight = point.y;
		click = new long[2];
	}
}
