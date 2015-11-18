package com.example.cellphonesaferactivity.Receiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.cellphonesaferactivity.Utils.DBUtils;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

public class DefenceBlackName extends BroadcastReceiver {

	private DBUtils dbUtils;
	private SQLiteDatabase db;
	private TelephonyManager tm;
	private AudioManager am;
	private static Context context1;
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
					//转接到空号
				Intent intent1 = new Intent();
				intent1.setData(Uri.parse("tel:**67*13800000000%23"));
				context1.startActivity(intent1);
				break;
			case 2:
					//
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		this.context1 = context;
		dbUtils = DBUtils.getInstance(context);
		db = dbUtils.getDatabase();
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			Cursor data = null;
			Object [] objects = (Object[])intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage message = SmsMessage.createFromPdu((byte [])object);
				String originatingAddress = message.getOriginatingAddress();
				try {
					data = db.query("blackuser", new String[]{"user_group"}, "number=?", new String[]{originatingAddress}, null, null, null);
					if(data.moveToFirst())
					{
						if(data.getInt(0)==0||data.getInt(0)==2)
						{
							abortBroadcast();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally{
					if(data!=null)
						data.close();
				}
			}
		}
		else if (action.equals("android.intent.action.PHONE_STATE")) {
			tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
			tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		}
		dbUtils.closeDatabase();
	}
	
	class MyPhoneStateListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Cursor data = null;
				try {
					data = db.query("blackuser", new String[]{"user_group"}, "number=?", new String[]{incomingNumber}, null, null, null);
					if(data.moveToFirst())
					{
						if(data.getInt(0)==0||data.getInt(1)==1)
						{
							//先静音处理
							am.setRingerMode(AudioManager.RINGER_MODE_SILENT); //非常重要，不然会响铃一下然后被终止
							endPhone();
							//恢复正常铃声					
							am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
							//0 正常endPhone()显示繁忙
							//1 转接到空号，显示用户已停机
							if(SharedPreferencesUtils.getInt("config", context1.getApplicationContext(), "refuse", 0)==1)
							{
								handler.dispatchMessage(handler.obtainMessage(1));
							}
						}	
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				finally{
					if(data!= null)
						data.close();
				}
				break;
			default:
				break;
			}
		}
	}

	public void endPhone() {
		// TODO Auto-generated method stub
		try {
			Method declaredMethod = TelephonyManager.class.getDeclaredMethod("getITelephony",(Class[]) null);
			declaredMethod.setAccessible(true);
			ITelephony iTelephony  = (ITelephony)declaredMethod.invoke(tm, (Object[]) null);
			iTelephony.endCall();
			Log.i("refuse", "success");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
