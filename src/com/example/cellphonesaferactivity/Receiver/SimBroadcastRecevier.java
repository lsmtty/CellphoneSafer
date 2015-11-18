package com.example.cellphonesaferactivity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

/**
 * 接收开机广播，查看sim卡是否更换
 * @author 思敏
 *
 */
public class SimBroadcastRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String numberInShare = SharedPreferencesUtils.getString("config",context ,"sim", null);//sim卡序列号
		if(TextUtils.isEmpty(numberInShare)||SharedPreferencesUtils.getBoolean("config",context ,"bind_cellphone", false))
		{
			Log.i("message", "未绑定SIM卡");
		}
		else {
			TelephonyManager simNumber = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
			if (simNumber.getSimSerialNumber().equals(numberInShare)) {
				//相同sim未更换
				Log.i("message", "sim卡未更换");
			}
			else {
				//发送短信到安全手机号
				Log.i("message", "sim卡被更换");
				Intent intent2 = new Intent();
				intent2.setAction(Intent.ACTION_CALL);
				//intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:18354225809"));
				String safenumber = SharedPreferencesUtils.getString("config", context, "safe_number", null);
				//发送报警短信
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenumber, null, "sim card is changed", null, null);
			}
		}
	}

}
