package com.example.cellphonesaferactivity.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * SharedPreference 操作工具类
 */
public class SharedPreferencesUtils 
{	
	/**
	 * 设置String 配置
	 * @param name  文件名
	 * @param context 上下文
	 * @param key	键值
	 * @param value 键值名
	 */
	public static  void setString(String name,Context context,String key,String value)
	{	
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	/**
	 * 设置int 配置
	 * @param name  文件名
	 * @param context 上下文
	 * @param key	键值
	 * @param value 键值名
	 */
	public static void setInt(String name,Context context,String key,int value)
	{
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * 设置boolean 配置
	 * @param name  文件名
	 * @param context 上下文
	 * @param key	键值
	 * @param value 键值名
	 */
	public static void setBoolean(String name,Context context,String key,Boolean value)
	{
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	/**
	 * 获取String 值
	 * @param name     文件名
	 * @param context  上下文
	 * @param key   	键值
	 * @param defValue  默认值
	 * @return
	 */
	public static String getString(String name,Context context,String key,String defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		return sp.getString(key, defValue);
	}
	/**
	 * 获取int  值
	 * @param name     文件名
	 * @param context  上下文
	 * @param key   	键值
	 * @param defValue  默认值
	 * @return
	 */
	public static int getInt(String name,Context context,String key,int defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		return sp.getInt(key, defValue);
	}
	/**
	 * 获取boolean  值
	 * @param name     文件名
	 * @param context  上下文
	 * @param key   	键值
	 * @param defValue  默认值
	 * @return
	 */
	public static Boolean getBoolean(String name,Context context,String key,Boolean defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(name, context.MODE_APPEND);
		return sp.getBoolean(key, defValue);
	}
}
