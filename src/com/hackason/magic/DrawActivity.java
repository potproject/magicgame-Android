package com.hackason.magic;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
public class DrawActivity extends Activity implements SensorEventListener
{
	   private SensorManager sensorManager;
	   private Sensor accelerometer;
	   private DrawView view;
	   String gametype="single";
	   public void onCreate(Bundle savedInstanceState)
	   {
	      super.onCreate(savedInstanceState);
	 
	      // 画面を縦表示で固定
	      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	 
	      view = new DrawView(this);
	      setContentView(view);
	      Intent intent = getIntent();
	      gametype= intent.getStringExtra("game");
	      //センサーマネージャの取得
	      sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
	 
	      //センサーの取得
	      List<Sensor> list;
	      list=sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
	      if (list.size()>0) accelerometer=list.get(0);
	   }
	 
	   //アプリの開始
	   protected void onResume() 
	   {
	      //アプリの開始
	      super.onResume();
	 
	      //センサーの処理の開始
	      if (accelerometer!=null) sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
	   }
	 
	   //アプリの停止
	   protected void onStop()
	   {
	      //センサーの処理の停止
	      sensorManager.unregisterListener(this);
	 
	      //アプリの停止
	      super.onStop();
	   }    
	 
	   //センサーリスナーの処理
	   public void onSensorChanged(SensorEvent event)
	   {
	      //加速度の取得
	      if (event.sensor==accelerometer)  view.setAcce(-event.values[0]*0.2f, event.values[1]*0.2f);
	   }
	   public void viewstop(int point){
		    if(gametype.equals("single")){
		    Intent intent;
			intent = new Intent(this, PointActivity.class);
			intent.putExtra("nowpoint", point);
			startActivity(intent);
			}else{
		    	Intent intent;
				intent = new Intent(this, BluetoothwaitActivity.class);
				intent.putExtra("nowpoint", point);
				startActivity(intent);
		    }
	   }
	 
	   //精度変更イベントの処理
	   public void onAccuracyChanged(Sensor sensor,int accuracy){}
	 
	   // 破棄の際に実行
	   public void onDestroy()
	   {
	      super.onDestroy();
	   }
	   @Override
	   public boolean dispatchKeyEvent(KeyEvent event){
	       //画面から離れた場合
	       if(event.getAction()==KeyEvent.ACTION_UP){
	           //戻るボタンの場合
	           if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
	               //trueを返して戻るのを無効化する
	               return true;
	           }
	       }
	       //通常通りの値を返す
	       return super.dispatchKeyEvent(event);
	   }
	}