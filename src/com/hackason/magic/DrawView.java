package com.hackason.magic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
	   private static double e = 0.8;
	   private static double d = 0.99;
	   private float pregx,gx;
	   private float pregy,gy;
	   private Paint paint;
	   private SurfaceHolder holder;
	   private Thread thread;
	   private int width;
	   private int height;
	   int pictX,pictY;
	   float gypoint,gxpoint;
	   float gymax,gxmax;
	   Bitmap bitmap;
	   int mLocateX,mLocateY;
	   int centerX,centerY;
	   Context context;
	   int point;
	   public DrawView(Context context)
	   {
	      super(context);
	      holder = null;
	      thread = null;
	      paint = new Paint();
	      //paint.setAntiAlias(true);
	      this.context=context;
	      gx = gy = 0;
	      point=0;
	      gypoint=(float) 0.0;
	      gxpoint=(float) 0.0;
	      gymax=0.0f;
	      gxmax=0.0f;
	      // getHolder()メソッドでSurfaceHolderを取得。さらにコールバックを登録
	      getHolder().addCallback(this);
	   }
	 
	   //SurfaceView生成時に呼び出される
	   public void surfaceCreated(SurfaceHolder holder)
	   {
		  Resources rsc = getResources();
	      bitmap =BitmapFactory.decodeResource(rsc, R.drawable.ic_launcher);
	      pictX = bitmap.getWidth();
	        pictY = bitmap.getHeight();
	        mLocateX = centerX=getWidth()/2;
	        mLocateY = centerY=getHeight()/2;
	      this.holder = holder;
	      thread = new Thread(this);
	      Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
				    //point集計
					Log.d("point",""+point);
					Log.d("gxmax",String.valueOf(gxmax-centerX));
					Log.d("gymax",String.valueOf(gymax-centerY));
					Log.d("centerx",String.valueOf(centerX));
					Log.d("centery",String.valueOf(centerY));
					//適当なほど点数下がる
					double dispoint=gxmax-centerX+gymax-centerY;
					//動かしていない疑惑
					dispoint =dispoint<20?20.0:dispoint;
					//大きすぎ減点
					if(dispoint>150){
						dispoint=dispoint/0.6;
						System.out.println("Oops");
					}
					Log.d("dispoint",""+dispoint);
					point=(int) (point/(dispoint/10));
					if(point>0){
						((DrawActivity)context).viewstop(point);
					}else{
						((DrawActivity)context).viewstop(0);
					}
				}
			}, 5000);//5000ms後に画面遷移する
	   }
	 
	   //SurfaceView変更時に呼び出される
	   public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	   {
	      if(thread != null )
	      {
	         this.width  = width;
	         this.height = height;
	         
	         thread.start();
	      }
	   }
	 
	   //SurfaceView破棄時に呼び出される
	   public void surfaceDestroyed(SurfaceHolder holder)
	   {
	      thread = null;
	   }
	 
	   //スレッドによるSurfaceView更新処理
	   public void doDraw()
	   {
	      while (thread != null)
	      {
	    	  if(gx > 0.001 || gx <-0.001 && gy > 0.001 || gy < -0.001){
	         //描画処理
	         Canvas canvas = holder.lockCanvas();
	         if(canvas!=null){
	        	 try{
	         //canvas.drawColor(Color.argb(255, 0, 0, 0));
	         paint.setStrokeWidth(12);
	         paint.setColor(Color.argb(255,148, 0, 211));
	         canvas.drawPoint(mLocateX-gy*50,mLocateY+gx*50, paint);
	         
             canvas.drawLine(mLocateX,mLocateY,mLocateX-gy*50,mLocateY+gx*50, paint);
	         mLocateX+=-gy*50;
	         mLocateY+=gx*50;
	         // 計算をボールに反映
	         point++;
	         gypoint=gy>0 ?gy : -gy;
	         gxpoint=gx>0 ?gx : -gx;
	         gymax= (centerY+(int)(gy*50))>gymax ? (centerY+(int)(gy*50)):gymax;
	         gxmax= (centerX+(int)(gx*50))>gxmax ? (centerX+(int)(gx*50)):gxmax;
	         point=(int) (point+(gypoint*gxpoint*100.0));
	         
	        	 }finally{
	        	 holder.unlockCanvasAndPost(canvas);
	         }}
	        }
	    	  }
	   }
	    @Override
	    public void run() {
	    		doDraw();

	    }

	 
	   // 加速度の更新
	   public void setAcce(float gx, float gy){
	      this.gx = gx;
	      this.gy = gy;
	   }
	}