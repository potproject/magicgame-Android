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
	       //‰æ–Ê‚©‚ç—£‚ê‚½ê‡
	       if(event.getAction()==KeyEvent.ACTION_UP){
	           //–ß‚éƒ{ƒ^ƒ“‚Ìê‡
	           if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
	               //true‚ğ•Ô‚µ‚Ä–ß‚é‚Ì‚ğ–³Œø‰»‚·‚é
	               return true;
	           }
	       }
	       //’Êí’Ê‚è‚Ì’l‚ğ•Ô‚·
	       return super.dispatchKeyEvent(event);
	   }
}
