package com.example.cellphonesaferactivity.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.R.string;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 系统进程工具类
 * @author 思敏
 *
 */
public class SystemInfoUtils {
	/**
	 * 判断进程是否在运行
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String className) 
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			String serviceClassName = info.service.getClassName();
			if(serviceClassName.equals(className))
				return true;
		}
		return false;
	}
	
	/**
	 * 获取正在运行的进程总数
	 * @param context
	 * @return
	 */
	public static int getProcessCount(Context context)
	{
		//得到进程管理者
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取到运行的进程列表，注意和服务列表进行区分
		List<RunningAppProcessInfo>  infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : infos) {
		}
		return infos.size();
	}
	/**
	 * 得到剩余内存
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		//获取内存的基本信息
		am.getMemoryInfo(memoryInfo);
		return memoryInfo.availMem;
	}
	
	/**
	 * 获取总内存大小,兼容低版本手机
	 * @return
	 */
	public static long getTotalMem(Context context)
	{
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(new File("/proc/meminfo"));
			reader = new BufferedReader(new InputStreamReader(fis));
			String readLine = reader.readLine();
			StringBuffer sb = new StringBuffer();

			for (char c : readLine.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			return Long.parseLong(sb.toString()) * 1024;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if (fis != null) {
					fis.close();
				}
				if(reader!=null)
				{
					reader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return 0 ;
	}	 
	
}


