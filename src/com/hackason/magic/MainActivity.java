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
		// �ʒm��I���������̃C���e���g
		preference = getSharedPreferences("setting", MODE_PRIVATE);  
    	int highscore = preference.getInt("highscore",0);
		Notification.Builder builder = new Notification.Builder(getApplicationContext());
		builder.setTicker("Battle of Magic circle");
		builder.setContentTitle("���Ȃ��̃n�C�X�R�A�́E�E�E");
		builder.setContentText(""+highscore+"���I");
		builder.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.getNotification();
		 
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(0, notification);
	}
	public void battleactivity(View v){
		BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
		if(!Bt.equals(null) && Bt.isEnabled()){
		    //Bluetooth�Ή��[���̏ꍇ�̏���
		    Log.d("battle","Bluetooth���T�|�[�g����Ă܂��B");
		    Intent intent;
		    intent = new Intent(this, BluetoothwaitActivity.class);
		    startActivity(intent);
		}else if(!Bt.isEnabled()){
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(i, REQUEST_ENABLE_BT);
		}else{
		    //Bluetooth��Ή��[���̏ꍇ�̏���
		    Toast.makeText(this, "Bluetooth���T�|�[�g����Ă��Ȃ����A�L���ɂȂ��Ă��Ȃ��̂ł��̋@�\�͎g�p�ł��܂���B���߂�Ȃ����B", Toast.LENGTH_LONG).show();
		    finish();
		}
	}
	public void onActivityResult(int requestCode, 
	int resultCode, Intent data){
	if(requestCode == REQUEST_ENABLE_BT){
	if(resultCode == RESULT_OK){
		Log.d("battle","Bluetooth���T�|�[�g����Ă܂��B");
		Intent intent;
		intent = new Intent(this, BluetoothwaitActivity.class);
	    startActivity(intent);
	}}}


}
