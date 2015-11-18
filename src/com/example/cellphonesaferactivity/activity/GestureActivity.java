package com.example.cellphonesaferactivity.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public abstract class GestureActivity extends Activity 
{
	private GestureDetector gd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub\
		gd = new GestureDetector(this,new MyGestrueManager());
		super.onCreate(savedInstanceState);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gd.onTouchEvent(event);// 委托手势识别器来处理手势
		return super.onTouchEvent(event);
	}
	class MyGestrueManager implements OnGestureListener
	{
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			float startX = e1.getX();
			float endX = e2.getX();
			Log.i("message","start:"+startX);
			Log.i("message","end:"+endX);
			if(Math.abs(velocityX)>200)  //左右滑动速度超过100px/s时 ***这个速度是带方向的 要特别注意
			{
				Log.i("message",""+velocityX);
				if(endX -startX>100)
				{//向右滑动
					Log.i("message", "toRight");
					moveToLeft();
					return true;
				}
				else if(startX - endX >100){//向左滑动
					Log.i("message", "toLeft");
					moveToRight();
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}

		
	}
	/**
	 * 向左滑屏，切换到右图
	 */
	public abstract void moveToRight();
	/**
	 * 向右滑屏，切换到左图
	 */
	public abstract void moveToLeft();
}
