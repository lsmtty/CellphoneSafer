package com.example.cellphonesaferactivity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
import com.example.cellphonesaferactivity.db.AddressDao;

public class ShowPhoneLocation extends BroadcastReceiver {
	private Context context;
	private TelephonyManager tm;
	private LayoutParams params;
	private WindowManager mWM;
	private LinearLayout rl;
	private int startX;
	private int startY;
	private int windowWidth;
	private int windowHeight;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		if (SharedPreferencesUtils.getBoolean("config", context,
				"phone_location", false)) {

			tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);
			tm.listen(new MyPhoneStateListener(),
					PhoneStateListener.LISTEN_CALL_STATE);
			mWM = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
	}

	class MyPhoneStateListener extends PhoneStateListener {


		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				onshowToast(AddressDao.location(incomingNumber));
				Log.i("message", "响铃");
				break;
			case TelephonyManager.CALL_STATE_IDLE:  //莫名问题，挂断电话会判断两次。。。，如果每次都removeView，第二次会报空异常
				Log.i("message", "挂断");
				/*if(mWM!=null)
				{
					Log.i("message", "mWM");
				}
				if(rl!=null)
				{
					Log.i("message", "rl");
				}*/
				if(rl!=null)  //判断rl是否存在再remove就ok了，一般移除子部分主要关心rl是否存在的问题S
				{
					mWM.removeView(rl);
				}	
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i("message", "接听");
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		/**
		 * 自定义归属地浮窗 需要权限android.permission.SYSTEM_ALERT_WINDOW
		 * 
		 * @param location
		 */
		private void onshowToast(String location) {
			//最新获取长宽的方法
			Display defaultDisplay = mWM.getDefaultDisplay();
			Point windowPoint = new Point();
			defaultDisplay.getSize(windowPoint);
			windowWidth = windowPoint.x;
			windowHeight = windowPoint.y;
			
			params = new WindowManager.LayoutParams();
			int lastX = SharedPreferencesUtils.getInt("config", context, "lastX", 0); //获取上次显示的x位置
			int lastY = SharedPreferencesUtils.getInt("config", context, "lastY", 0); //获取上次显示的Y位置
			
			//设置悬浮窗的位置，基于左上方的偏移量
			params.x = lastX;
			params.y = lastY;
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			rl = (LinearLayout) LinearLayout.inflate(context,R.layout.active_toast_layout, null);
			params.format  = PixelFormat.TRANSLUCENT;
			params.type = WindowManager.LayoutParams.TYPE_PHONE;//电话窗口。用于交互
			params.gravity = Gravity.START + Gravity.TOP;
			TextView tv = (TextView) rl.findViewById(R.id.tv_phone_location);
			//初始化样式文件
			int[] bgs = new int[] { R.drawable.call_locate_orange,
					R.drawable.call_locate_blue, R.drawable.call_locate_gray,
					R.drawable.call_locate_green,R.drawable.call_locate_white};
			int style = SharedPreferencesUtils.getInt("config", context,"style", 0);
			rl.setBackgroundResource(bgs[style]); // 根据样式显示
			tv.setText(location);
			mWM.addView(rl, params); // 将view添加到屏幕上
			rl.setOnTouchListener(new OnTouchListener() {

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
						params.x +=dx;
						params.y +=dy;
						if(params.x<0)
						{
							params.x = 0;
						}
						if(params.y<0)
						{
							params.y = 0;
						}
						if(params.x>windowWidth-v.getWidth())
						{
							params.x = windowWidth-v.getWidth();
						}
						if(params.y>windowHeight-v.getHeight())
						{
							params.y = windowHeight-v.getHeight();
						}
						//更新布局
						mWM.updateViewLayout(rl, params);
						//重新初始化坐标
						startX = endX;
						startY = endY;
						break;
					case MotionEvent.ACTION_UP:
						SharedPreferencesUtils.setInt("config", context, "lastX", params.x);
						SharedPreferencesUtils.setInt("config", context, "lastY", params.y);
						break;
					default:
						break;
					}
					return true;
				}
			});
		}
	}
}
