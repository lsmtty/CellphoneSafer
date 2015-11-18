package com.example.cellphonesaferactivity.Utils;

import com.example.cellphonesaferactivity.db.MyDbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DBUtils 
{
	private SQLiteDatabase db;
	private static Context context;
	private static DBUtils dbUtils;
	private static int number = 0;
	
	/**
	 * 获得数据库实例
	 * @return  上下文
	 */
	public SQLiteDatabase getDatabase() 
	{
		
		MyDbHelper helper = new MyDbHelper(context, "blackUsers.db", null, 1);
		db = helper.getWritableDatabase();
		return db;
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDatabase()
	{
		if(db!=null)
			db.close();
	}
	/**
	 * 获得唯一的一个数据库操作类
	 * @param activity
	 * @return
	 */
	public static DBUtils getInstance(Context context)
	{
		if(number==0)
		{
			dbUtils = new DBUtils(context);
			number = 1;
			return dbUtils;
		}
		else {
			return dbUtils;
		}
	}
	private  DBUtils(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
}