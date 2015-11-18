package com.example.cellphonesaferactivity.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager.Query;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cellphonesafer.R;
import com.example.cellphonesaferactivity.Beans.BlackUsers;
import com.example.cellphonesaferactivity.Utils.DBUtils;

public class BlackNameActivity extends Activity 
{
	private ListView lv;
	private Button addButton;
	private static final String [] GROUP = new String []{"拦截全部","拦截电话","拦截短信"};
	private List<BlackUsers> blackUsers;
	private AlertDialog addDialog;
	private AlertDialog changeDialog;
	private DBUtils dbUtils;
	private SQLiteDatabase database;
	private int choose = 0;
	private Myadapter myadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communicate_protector_layout);
		init();
		initLisener();
		initData();
	}

	private void initLisener() {
		// TODO Auto-generated method stub
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addBlackName();
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 添加黑名单
	 */
	protected void addBlackName() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(getResources().getDrawable(R.drawable.abc_ab_bottom_solid_dark_holo));
		builder.setTitle("请输入您想要拉黑的手机号");
		View view = getLayoutInflater().inflate(R.layout.alert_text_layout, null);
		final EditText tv  = (EditText) view.findViewById(R.id.et_text);
		tv.setVisibility(View.VISIBLE);
		RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
		RadioButton fb1 = (RadioButton) view.findViewById(R.id.rb_all);
		
		fb1.setChecked(true);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_all:
					choose = 0;
					break;
				case R.id.rb_call:
					choose = 1;
					break;
				case R.id.rb_msg:
					choose = 2;
					break;
				default:
					break;
				}
			}
		});
		builder.setView(view);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(!tv.getText().toString().isEmpty())
				{
					insert(tv.getText().toString(),choose);
				}
				addDialog.dismiss();
			}
		});
		addDialog = builder.show();
	}
	
	/**
	 * 删除黑名单
	 */
	private void delete(int id) {
		// TODO Auto-generated method stub
		database.delete("blackuser", "id=?", new String[]{""+id+""});
		myadapter.notifyDataSetChanged();
		Toast.makeText(BlackNameActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
	}
	/**
	 * 改变黑名单禁止信息
	 * @param id
	 * @param choose
	 */
	private void update(int id, int choose) {
		// TODO Auto-generated method stub
		Log.i("changeGroup", ""+choose);
		ContentValues contentValues = new ContentValues();
		contentValues.put("user_group", choose);
		database.update("blackuser", contentValues, "id=?", new String[]{""+id+""});//查询的是数字，直接写"id",当然不会有结果
		myadapter.notifyDataSetChanged();
	}
	/**
	 * 添加黑名单进入数据库
	 */
	protected void insert(String number,int group) {
		//Log.i("message", number+":group="+group);
		// TODO Auto-generated method stub
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("user_group", group);
		database.insert("blackuser", null, contentValues);
		Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
		//order by 不能只写ASC或者DESC还应该写根据哪一列排列
		Cursor only = database.query("blackuser",new String[]{"id"}, "number=?", new String[]{number}, null, null, "id DESC", null);
		only.moveToNext();
		int id = only.getInt(0);
		blackUsers.add(new BlackUsers(id, number, group));
		myadapter.notifyDataSetChanged();
	}

	private void initData() {
		// TODO Auto-generated method stub
		//异步加载数据
		Cursor dataCursor = database.query("blackuser", new String[]{"id","number","user_group"}, null, null, null, null,null);
		if(dataCursor!=null)
		{
			while(dataCursor.moveToNext())
			{
				int id = dataCursor.getInt(0);
				String number = dataCursor.getString(1);
				int group = dataCursor.getInt(2);
				blackUsers.add(new BlackUsers(id, number, group));
			}
			dataCursor.close();
		}
	}

	private void init() {
		lv = (ListView) findViewById(R.id.lv_blacknames);
		addButton = (Button) findViewById(R.id.bt_add);
		blackUsers = new ArrayList<BlackUsers>();
		dbUtils = DBUtils.getInstance(this);
		database = dbUtils.getDatabase();
		myadapter = new Myadapter();
		lv.setAdapter(myadapter);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		/*dbUtils.closeDatabase();*/
		super.onDestroy();
	}
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blackUsers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return blackUsers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder holder =null ;
			final BlackUsers user = blackUsers.get(position);
			if(convertView ==null)
			{
				view = View.inflate(BlackNameActivity.this, R.layout.blackname_item, null);
				holder = new ViewHolder(view);
				holder.phone = (TextView) view.findViewById(R.id.tv_phone);
				holder.msg = (TextView) view.findViewById(R.id.tv_msg);
				holder.delete = (ImageView) view.findViewById(R.id.iv_delete);
				holder.msg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new Builder(BlackNameActivity.this);
						builder.setIcon(getResources().getDrawable(R.drawable.abc_ab_bottom_solid_dark_holo));
						builder.setTitle("更改拦截设置");
						View view = getLayoutInflater().inflate(R.layout.alert_text_layout, null);
						final TextView tv  = (TextView) view.findViewById(R.id.tv_text);
						tv.setVisibility(View.VISIBLE);
						tv.setText("号码:"+user.getNumber());
						RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
						switch (user.getGroup()) {
						case 0:
							RadioButton rg1 = (RadioButton) view.findViewById(R.id.rb_all);
							rg1.setChecked(true);
							break;
						case 1:
							RadioButton rg2 = (RadioButton) view.findViewById(R.id.rb_call);
							rg2.setChecked(true);
							break;
						case 2:
							RadioButton rg3 = (RadioButton) view.findViewById(R.id.rb_msg);
							rg3.setChecked(true);
							break;
						default:
							break;
						}
						rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								// TODO Auto-generated method stub
								switch (checkedId) {
								case R.id.rb_all:
									choose = 0;
									break;
								case R.id.rb_call:
									choose = 1;
									break;
								case R.id.rb_msg:
									choose = 2;
									break;
								default:
									break;
								}
							}
						});
						builder.setView(view);
						builder.setPositiveButton("确定", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
									update(user.getId(),choose);
									user.setGroup(choose);
								changeDialog.dismiss();
							}
						});
						changeDialog = builder.show();
					}
				});
				holder.delete.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//队列中移除
						blackUsers.remove(user);
						//数据库中移除
						delete(user.getId());
					}
				});
				view.setTag(holder);
			}
			else {
				holder =(ViewHolder) view.getTag();
			}
			//动态查询填入listview中
			holder.getMsg().setText(GROUP[blackUsers.get(position).getGroup()]);
			holder.getPhone().setText(blackUsers.get(position).getNumber());
			return view;
		}
		
		class ViewHolder {
			View baseview;
			TextView phone;
			TextView msg;
			ImageView delete;
			public ViewHolder(View view) {
				// TODO Auto-generated constructor stub
				this.baseview = view;
			}
			public TextView getPhone() 
			{
				if(phone==null)
				{
					phone = (TextView)baseview.findViewById(R.id.tv_phone);
				}
				return phone;
			}
			public TextView getMsg() {
				if(msg==null)
				{
					msg = (TextView) baseview.findViewById(R.id.tv_msg);
				}
				return  msg;

			}
			public ImageView getIv() 
			{
				if(delete==null)
				{
					delete =  (ImageView) baseview.findViewById(R.id.iv_delete);
				}
				return  delete;
			}
		}
	}
}
