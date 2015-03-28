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
	 
	      // ��ʂ��c�\���ŌŒ�
	      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	 
	      view = new DrawView(this);
	      setContentView(view);
	      Intent intent = getIntent();
	      gametype= intent.getStringExtra("game");
	      //�Z���T�[�}�l�[�W���̎擾
	      sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
	 
	      //�Z���T�[�̎擾
	      List<Sensor> list;
	      list=sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
	      if (list.size()>0) accelerometer=list.get(0);
	   }
	 
	   //�A�v���̊J�n
	   protected void onResume() 
	   {
	      //�A�v���̊J�n
	      super.onResume();
	 
	      //�Z���T�[�̏����̊J�n
	      if (accelerometer!=null) sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
	   }
	 
	   //�A�v���̒�~
	   protected void onStop()
	   {
	      //�Z���T�[�̏����̒�~
	      sensorManager.unregisterListener(this);
	 
	      //�A�v���̒�~
	      super.onStop();
	   }    
	 
	   //�Z���T�[���X�i�[�̏���
	   public void onSensorChanged(SensorEvent event)
	   {
	      //�����x�̎擾
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
	 
	   //���x�ύX�C�x���g�̏���
	   public void onAccuracyChanged(Sensor sensor,int accuracy){}
	 
	   // �j���̍ۂɎ��s
	   public void onDestroy()
	   {
	      super.onDestroy();
	   }
	   @Override
	   public boolean dispatchKeyEvent(KeyEvent event){
	       //��ʂ��痣�ꂽ�ꍇ
	       if(event.getAction()==KeyEvent.ACTION_UP){
	           //�߂�{�^���̏ꍇ
	           if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
	               //true��Ԃ��Ė߂�̂𖳌�������
	               return true;
	           }
	       }
	       //�ʏ�ʂ�̒l��Ԃ�
	       return super.dispatchKeyEvent(event);
	   }
	}