package com.hackason.magic;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static final int REQUEST_ENABLE_BT = 0;
	private SharedPreferences preference;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void stayactivity(View v){
		Intent intent;
		intent = new Intent(this, DrawActivity.class);
		intent.putExtra("game", "single");
		startActivity(intent);
	}
	public void wear(View v){
		// 通知を選択した時のインテント
		preference = getSharedPreferences("setting", MODE_PRIVATE);  
    	int highscore = preference.getInt("highscore",0);
		Notification.Builder builder = new Notification.Builder(getApplicationContext());
		builder.setTicker("Battle of Magic circle");
		builder.setContentTitle("あなたのハイスコアは・・・");
		builder.setContentText(""+highscore+"だ！");
		builder.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.getNotification();
		 
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(0, notification);
	}
	public void battleactivity(View v){
		BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
		if(!Bt.equals(null) && Bt.isEnabled()){
		    //Bluetooth対応端末の場合の処理
		    Log.d("battle","Bluetoothがサポートされてます。");
		    Intent intent;
		    intent = new Intent(this, BluetoothwaitActivity.class);
		    startActivity(intent);
		}else if(!Bt.isEnabled()){
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(i, REQUEST_ENABLE_BT);
		}else{
		    //Bluetooth非対応端末の場合の処理
		    Toast.makeText(this, "Bluetoothがサポートされていないか、有効になっていないのでこの機能は使用できません。ごめんなさい。", Toast.LENGTH_LONG).show();
		    finish();
		}
	}
	public void onActivityResult(int requestCode, 
	int resultCode, Intent data){
	if(requestCode == REQUEST_ENABLE_BT){
	if(resultCode == RESULT_OK){
		Log.d("battle","Bluetoothがサポートされてます。");
		Intent intent;
		intent = new Intent(this, BluetoothwaitActivity.class);
	    startActivity(intent);
	}}}


}
