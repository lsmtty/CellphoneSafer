package com.example.cellphonesaferactivity.Provider;

import com.example.cellphonesaferactivity.db.MySoftwareDbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SoftwareProvider extends ContentProvider {

	public static UriMatcher softMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private MySoftwareDbHelper mySoftwareDbHelper;
	private SQLiteDatabase sqlDB;
	static {
		softMatcher.addURI("lsmtty.example.softprovider","software",1);
	}
	@Override
	public boolean onCreate() {
		mySoftwareDbHelper = new MySoftwareDbHelper(getContext(), "software", null, 1);
		sqlDB = mySoftwareDbHelper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor query = sqlDB.query("software", projection, selection, selectionArgs, null, null, sortOrder);
		return query;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		if(softMatcher.match(uri)==1)
		{
			long insert = sqlDB.insert("software", null, values);
			getContext().getContentResolver().notifyChange(uri, null); //只有provider发送数据更改通知，才可以被内容观察者观察到
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int length = 0;
		if(softMatcher.match(uri)==1)
		{
			length = sqlDB.delete("software", selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null); //只有provider发送数据更改通知，才可以被内容观察者观察到
		}
		return length;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
