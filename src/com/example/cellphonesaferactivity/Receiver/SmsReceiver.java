package com.example.cellphonesaferactivity.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;
import com.example.cellphonesaferactivity.service.LocationService;
/**
 * 接收报警短信,做出报警反应(这部分尤其是设备管理器的部分加入笔记，或者放进学习博客)
 * @author 思敏
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	private DevicePolicyManager dm;
	private ComponentName componentName;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object [] objects = (Object[])intent.getExtras().get("pdus");
		dm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(context, AdminReceiver.class);
		for (Object object : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[])object);
			String originatingAddress = message.getOriginatingAddress(); 
			if(originatingAddress.equals(SharedPreferencesUtils.getString("config", context, "safe_number", null)))
			{
				String messageBody = message.getMessageBody();
				Log.i("mssageBody", messageBody);
				if("#*alarm*#".equals(messageBody))
				{
					MediaPlayer mp = MediaPlayer.create(context, R.raw.ylzs);
					mp.setVolume(1f, 1f); //1f就是最大，并且这里是程声音并不是接听和消息，不会和静音冲突
					mp.setLooping(true);
					mp.start();
					abortBroadcast();
				}
				else if ("#*location*#".equals(messageBody)) {
					//从config中读出位置信息
					context.startService(new Intent(context,LocationService.class));//收到消息才开启服务，并不是一直开着
					abortBroadcast();
				}
				else if("#*wipedata*#".equals(messageBody)){
					Toast.makeText(context, "您的数据已经擦除", Toast.LENGTH_LONG).show();
					/*if(dm.isAdminActive(componentName))  过于危险
					{
						//擦除数据
						dm.wipeData(0);
						abortBroadcast();
					}*/
				}
				else if("#*lockscreen*#".equals(messageBody)){
					Log.i("message", ""+dm.isAdminActive(componentName));
					if(dm.isAdminActive(componentName))
					{
						//立刻锁屏
						dm.lockNow();
						//设置开屏密码
						dm.resetPassword("123456", 0);
						abortBroadcast();
					}
				}
			}
		}
		
	}
}
