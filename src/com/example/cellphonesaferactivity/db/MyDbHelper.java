package com.example.cellphonesaferactivity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

	public MyDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//设为自增，如果入为null，引擎会自动设为romid+1
		db.execSQL("create table blackuser (id INTEGER PRIMARY KEY AUTOINCREMENT,number VARCHAR(20),user_group INTEGER default '0')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
