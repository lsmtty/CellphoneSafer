package com.example.cellphonesaferactivity.testcase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.cellphonesaferactivity.Utils.DBUtils;
import com.example.cellphonesaferactivity.db.MyDbHelper;
import com.example.cellphonesaferactivity.db.MySoftwareDbHelper;

public class DBTestCase extends AndroidTestCase 
{
	private DBUtils dbUtils;
	private SQLiteDatabase db;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		dbUtils = DBUtils.getInstance(getContext());
		db = dbUtils.getDatabase();
	}
	
	public void dbInsert()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", 1);
		contentValues.put("number", 10086);
		contentValues.put("user_group", 0);
		db.insert("blackuser", null, contentValues);
		Cursor query = db.query("blackuser",new String[]{"number"} , "id=?", new String[]{"1"}, null, null, null);
		query.moveToNext();
		assertEquals("10086", query.getString(0));
	}
	public void dbQuery() {
		

		MySoftwareDbHelper helper = new MySoftwareDbHelper(getContext(), "software.db", null, 1);
		db = helper.getWritableDatabase();
	}
	public void update()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("user_group", 3);
		db.update("blackuser", contentValues, "id=?", new String[]{"id"});
	}
	
	public void delete() {
		db.delete("blackuser", "id=?", new String[]{"id"});
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		dbUtils.closeDatabase();
		super.tearDown();
	}
}
