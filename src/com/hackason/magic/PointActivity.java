package com.hackason.magic;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PointActivity extends Activity {
	private SharedPreferences preference;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);
		Intent intent = getIntent();
		int ipoint = intent.getIntExtra("nowpoint",0);
		TextView tpoint=(TextView)findViewById(R.id.textpoint);
		
		
		//hightscore�̃Z�b�g
		preference = getSharedPreferences("setting", MODE_PRIVATE);  
        TextView hight=(TextView)findViewById(R.id.hightext);
    	int highscore = preference.getInt("highscore",0);
    	//highscore�X�V�I
    	if(highscore<ipoint){
    		editor = preference.edit(); 
    		editor.putInt("highscore", ipoint);  
            editor.commit(); 
            highscore=ipoint;
            hight.setTextColor(Color.RED);
            tpoint.setTextColor(Color.RED);
    	}
    	
		hight.setText("highScore:"+highscore);
		tpoint.setText(""+ipoint);
	}
	public void returnactivity(View v){
		Intent intent;
		intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	public void retry(View v){
		Intent intent;
		intent = new Intent(this, DrawActivity.class);
		intent.putExtra("game", "single");
		startActivity(intent);
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
