package com.example.cellphonesaferactivity.service;

import com.example.cellphonesaferactivity.Utils.SharedPreferencesUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 获取手机地理位置，存入配置文件
 * @author 思敏
 *
 */
public class LocationService extends Service {

	private String bestProvider;
	private LocationManager lm;
	private MyLocationListener ml;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lm = (LocationManager)getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = lm.getBestProvider(criteria, true);
		ml = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, ml);
		return super.onStartCommand(intent, flags, startId);
	}
	class MyLocationListener implements LocationListener{

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//位置改变后触发
			SharedPreferencesUtils.setString("config",getApplicationContext(), "location", "j:"+location.getLongitude()+";w:"+location.getLatitude());
			String location2 = SharedPreferencesUtils.getString("config", getApplicationContext(), "location", null);
			Uri uri = Uri.parse("smsto:"+SharedPreferencesUtils.getString("config",getApplicationContext(), "safe_number", null));
			Intent intent2 = new Intent(Intent.ACTION_SENDTO,uri);
			intent2.putExtra("sms_body", "location:"+location2);
			startActivity(intent2);
			stopSelf();//终止自己服务
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		lm.removeUpdates(ml);  //当activity销毁时，停止更新位置,节省电量
		super.onDestroy();
	}
}
