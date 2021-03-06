package com.hackason.magic;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class BattleActivity extends Activity {
	static final int REQUEST_ENABLE_BT = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
