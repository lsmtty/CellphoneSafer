package com.example.cellphonesaferactivity.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDao 
{
	private static final String PHONE_REGRESSION ="^1[34578][0-9]{9}$";
	private static final String PATH = "data/data/com.example.cellphonesafer/files/address.db";
	private static String location;
	public static String location(String number)
	{
		if(number.length()==3)
		{
			return "报警电话";
		}
		else if(number.length()==4)
		{
			return "模拟电话";
		}
		else if(number.length()==5)
		{
			return "客服电话";
		}
		else if((number.length()==9||number.length()==10)&&number.startsWith("0"))
		{
			return "地方号码";
		}
		else if(number.length()==11 && number.matches(PHONE_REGRESSION)){
			SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
			Log.i("addressDB", database.getPath());
			Cursor data = null;
			try {
				data = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});
				data.moveToNext();
				location = data.getString(0);
			}
			finally
			{
				if(data!=null)
					data.close();
			}
			return location;
		}
		else {
			return "未知号码";
		}
	}
}
