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
