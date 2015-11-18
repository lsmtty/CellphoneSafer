package com.example.cellphonesaferactivity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.cellphonesafer.R;

/**
 * 选择联系人
 * @author 思敏
 *
 */
public class ChooseContactor extends Activity 
{
	private ListView lv;
	private List<Map<String,Object>> contactors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactor_layout);
		init();
		initData();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				if(contactors!=null)
				{
					bundle.putString("name", (String)(contactors.get(position).get("name")));
					bundle.putString("phone", (String)(contactors.get(position).get("phone")));
				}
				else {
					bundle.putString("phone", "");
				}
				setResult(RESULT_OK, new Intent().putExtras(bundle));
				finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		initList();
		lv.setAdapter(new SimpleAdapter(this, contactors, R.layout.contact_item_layout, new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_number}));
	}

	/**
	 * 注意有些手机的数据库中有空字段，必须判断为空，不然肯定崩溃
	 */
	private void initList() {
		// TODO Auto-generated method stub
		Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri data = Uri.parse("content://com.android.contacts/data");
		Cursor rawContactsCursor = getContentResolver().query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
		Log.i("rawContactsCursor","" +rawContactsCursor.getCount());
		if(rawContactsCursor!=null)
		{
			while(rawContactsCursor.moveToNext())
			{
				if(rawContactsCursor.isNull(0))    //联系人ID都能为空，不用说了，不管这项罢了
					continue;
				String contactid = rawContactsCursor.getString(rawContactsCursor.getColumnIndex("contact_id"));
				Cursor dataCursor = getContentResolver().query(data, new String[]{"data1","mimetype"}, "contact_id=?", new String[]{contactid}, null);
				if(dataCursor!=null)  //取出某一个联系人的所有信息
				{
					Map<String,Object> map = new HashMap<String, Object>();
					while(dataCursor.moveToNext())
					{
						String mimetype = null;
						if(dataCursor.isNull(0))  //数据内容为空，则相应数据类型写为空
						{
							mimetype = dataCursor.getString(1);
							if("vnd.android.cursor.item/phone_v2".equals(mimetype))
							{
								map.put("phone", "电话号码为空");
							}
							else if("vnd.android.cursor.item/name"
									.equals(mimetype))
							{
								map.put("name", "姓名为空");
							}
						}
						if(dataCursor.isNull(1))		//数据MIME类型为空，则跳过
						{
							continue;
						}
						//正常数据正常判断类型，写入
						String data1 = dataCursor.getString(0);
						mimetype = dataCursor.getString(1);
						if("vnd.android.cursor.item/phone_v2".equals(mimetype))
						{
							map.put("phone", data1);
						}
						else if("vnd.android.cursor.item/name"
								.equals(mimetype))
						{
							map.put("name", data1);
						}
					}
					contactors.add(map);
					dataCursor.close();  //下一次获取联系人就不会重复了
				}
			}
			rawContactsCursor.close();
		}
	}

	private void init() {
		lv = (ListView) findViewById(R.id.lv);
		contactors = new ArrayList<Map<String,Object>>();
	}
}
