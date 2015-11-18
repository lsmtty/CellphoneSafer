package com.example.cellphonesaferactivity.activity;
import java.util.ArrayList;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Beans.AppInfo;
import com.example.cellphonesaferactivity.Beans.ScanedSoftware;
import com.example.cellphonesaferactivity.Utils.MD5Utils;
import com.example.cellphonesaferactivity.Utils.SoftWareUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class KillVirus extends Activity {
	private ImageView scan;
	private TextView findVirus;
	private ProgressBar pb;
	private LinearLayout ll;
	private ScrollView scroll;
	protected static final int BEGING = 1; 
	protected static final int SCANNING = 2; 
	protected static final int FINISH = 3; 
	private Handler Handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BEGING:
				findVirus.setText("开始绝妙的杀毒之旅");
				break;
			case SCANNING:
				TextView siglScaned = new TextView(KillVirus.this);
				ScanedSoftware ss = (ScanedSoftware)msg.obj;
				if(ss.isVirus())
				{
					siglScaned.setText(ss.getAppName()+"是病毒");
				}
				else {
					siglScaned.setText(ss.getAppName()+"可以放心使用");
				}
				ll.addView(siglScaned);
				//设置成可以自由滚动的条形
				scroll.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//一致往下面进行滚动！！！！
						scroll.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
				break;
			case FINISH:
				scan.clearAnimation();  //记住移除动画不是remove而是 clear
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kill_virus_layout);
		initUI();
		initData();
	}
	
	private void initUI() {
		scan = (ImageView) findViewById(R.id.iv_scanning);
		findVirus = (TextView) findViewById(R.id.tv_init_virus);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		ll = (LinearLayout) findViewById(R.id.ll_content);
		scroll = (ScrollView) findViewById(R.id.scrollView);
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画的时间
		rotateAnimation.setDuration(5000);
		// 设置动画无限循环
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		// 开始动画
		scan.startAnimation(rotateAnimation);
	}
	private void initData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//获取安装程序数量
				Handler.sendMessage(Handler.obtainMessage(BEGING));
				int i = 0;
				ArrayList<AppInfo> softWareList = SoftWareUtils.getSoftWareList(KillVirus.this);
				int count = softWareList.size();
				pb.setMax(count);
				int progress = 0;
				for (AppInfo appInfo : softWareList) {
					ScanedSoftware ss = null;
					String soureDir = appInfo.getPath();
					String md5String = MD5Utils.getMD5String(soureDir);
					i++;
					progress++;
					SystemClock.sleep(100);
					pb.setProgress(progress);
					if(i%15==0)
					{
						ss = new ScanedSoftware(appInfo.getApkName(), appInfo.getPackageName(), true);
					}
					else {
						ss = new ScanedSoftware(appInfo.getApkName(), appInfo.getPackageName(), false);
					}
					Message msg = Handler.obtainMessage();
					msg.what = SCANNING;
					msg.obj = ss;
					Handler.sendMessage(msg);
				}
				Handler.sendMessage(Handler.obtainMessage(FINISH));
			}
		}).start();
	}

}
