package com.hackason.magic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
	public class BluetoothwaitActivity extends Activity implements OnClickListener {

		  /**
			 * ログ用タグ
			 */
			private static final String TAG = "BLUETOOTH_SAMPLE";
		 
			/**
			 * Bluetoothアダプタ
			 */
			private BluetoothAdapter mAdapter;
		 
			/**
			 * ペアリング済みBluetoothDevice名を入れるArray
			 */
			private ArrayList<BluetoothDevice> mDevices;
			int ipoint;
			/**
			 * Button1
			 */
			//private Button mButton1;
		 
			/**
			 * Button2
			 */
			private Button mButton2;
		 
			/**
			 * Button3
			 */
			private Button mButton3;
		 
			/**
			 * Button4
			 */
			private Button mButton4;
			private Button mButton5;
			/**
			 * SPPのUUID
			 */
			private UUID MY_UUID = UUID.fromString("1111111-1234-1000-1111-00AAABCDEFFF");
		 
			/**
			 * ServerThread
			 */
			private ServerThread serverThread;
		 
			/**
			 * ClientThread
			 */
			private ClientThread clientThread;
			Handler mHandler;
			/**
			 * 自分のBluetooth端末の名前
			 */
			private static final String NAME = "BLUETOOTH_ANDROID";
		 
			/**
			 * 接続時のデータ送受信処理のためのThread
			 */
			private ConnectedThread connection;
			
			TextView myscore;
			static TextView yourscore;
			static TextView tText;
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_btwait);
				//ハンドラを生成
		        mHandler = new Handler();
		 
				// Get Device Listボタン
				//mButton1 = (Button) findViewById(R.id.buttona);
				//mButton1.setOnClickListener(this);
		 
				// Start Serverボタン
				mButton2 = (Button) findViewById(R.id.buttonb);
				mButton2.setOnClickListener(this);
		 
				// Start Clientボタン
				mButton3 = (Button) findViewById(R.id.buttonc);
				mButton3.setOnClickListener(this);
		 
				// Messageを送付
				mButton4 = (Button) findViewById(R.id.buttond);
				mButton4.setOnClickListener(this);
				mButton5 = (Button) findViewById(R.id.buttone);
				mButton5.setOnClickListener(this);
				myscore=(TextView)findViewById(R.id.textmy);
				yourscore=(TextView)findViewById(R.id.textyour);
				tText=(TextView)findViewById(R.id.textView1);
				Intent intent = getIntent();
				ipoint = intent.getIntExtra("nowpoint",0);
				myscore.setText("あなたのスコア:"+ipoint);
				
			}
			
			@Override
			public void onClick(View view) {
				// ペアリング済みデバイスリストを取得する
				if (view.equals(mButton2) || view.equals(mButton3)) {
					mDevices = new ArrayList<BluetoothDevice>();
					mAdapter = BluetoothAdapter.getDefaultAdapter();
					Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
		 
					// ペアリング済みデバイスのリスト
					for (BluetoothDevice device : devices) {
						mDevices.add(device);
						// Toastで表示する
						//Toast.makeText(this, "Name:" + device.getName(), Toast.LENGTH_LONG).show();
					}
				}
				// Serverを起動
				if (view.equals(mButton2)) {
					serverThread = new ServerThread();
					serverThread.start();
					tText.setText("サーバーとして待機中・・・");
				}
				// Clientを起動
				else if (view.equals(mButton3)) {
					if (mDevices != null) {
						for (int i = 0; i < mDevices.size(); i++) {
							clientThread = new ClientThread(mDevices.get(i));
							clientThread.start();
							tText.setText("クライアントとして待機中・・・");
						}
					}
				}
				// ゲーム開始
				else if (view.equals(mButton4)) {
					Intent intent;
					intent = new Intent(this, DrawActivity.class);
					intent.putExtra("game", "double");
					startActivity(intent);
				}
				//点数同期
				else if (view.equals(mButton5)) {
					String message = ""+ipoint;
					if (connection != null) {
						connection.write(message.getBytes());
					}else{
						Toast.makeText(this,"Bluetooth接続ができておりません。", Toast.LENGTH_SHORT).show();
					}

				}
			}
		 
			/**
			 * ServerのThread
			 */
			private class ServerThread extends Thread {
				private final BluetoothServerSocket mmServerSocket;
		 
				public ServerThread() {
					BluetoothServerSocket tmp = null;
					try {
						// MY_UUIDでSPPのUUIDを指定
						tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
					} catch (IOException e) {
					}
					mmServerSocket = tmp;
				}
		 
				/**
				 * Runメソッド
				 */
				public void run() {
					BluetoothSocket socket = null;
		 
					/**
					 * Whileループの中で常時Clientからの接続待機でPolling
					 */
					while (true) {
						Log.i(TAG, "Polling");
						try {
							socket = mmServerSocket.accept();
		 
						} catch (Exception e) {
							break;
						}
		 
						// Clientが接続するとsocketがnullではなくなる
						if (socket != null) {
							// 接続されると呼び出される
							manageConnectedSocket(socket);
							try {
								mmServerSocket.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
				}
		 
				/**
				 * 接続が終了する時呼ばれる
				 */
				public void cancel() {
					try {
						mmServerSocket.close();
					} catch (IOException e) {
					}
				}
			}
		 
			/**
			 * Client用のThread
			 */
			private class ClientThread extends Thread {
				private final BluetoothSocket mmSocket;
				private final BluetoothDevice mmDevice;
		 
				public ClientThread(BluetoothDevice device) {
		 
					BluetoothSocket tmp = null;
					mmDevice = device;
		 
					try {
						// SPPのUUIDを指定
						// この処理には android.permission.BLUETOOTH_ADMIN のパーミッションが必要
						tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
					} catch (Exception e) {
						Log.i(TAG, "Error:" + e);
					}
					mmSocket = tmp;
					
				}
		 
				public void run() {
		 
					// Discoveryモードを終了する
					mAdapter.cancelDiscovery();
		 
					try {
						// サーバに接続
						mmSocket.connect();
					} catch (IOException connectException) {
		 
						try {
							mmSocket.close();
						} catch (IOException closeException) {
						}
						return;
					}
		 
					// 接続されると呼び出される
					manageConnectedSocket(mmSocket);
				}
		 
				/**
				 * 接続を終了する際に呼ばれる
				 */
				public void cancel() {
					try {
						mmSocket.close();
					} catch (IOException e) {
					}
				}
			}
		 
			/**
			 * Server, Client共通 接続が確立した際に呼び出される
			 */
			public void manageConnectedSocket(BluetoothSocket socket) {
				Log.i(TAG, "Connection");
				connection = new ConnectedThread(socket);
				connection.start();
				mHandler.post(new Runnable() {
	                //run()の中の処理はメインスレッドで動作されます。
	                public void run() {
	                	tText.setText("Bluetooth接続完了！");
	                }
				});
				
			}
		 
			/**
			 * 接続確立時のデータ送受信用のThread
			 */
			private class ConnectedThread extends Thread {
				private final BluetoothSocket mmSocket;
				private final InputStream mmInStream;
				private final OutputStream mmOutStream;
		 
				public ConnectedThread(BluetoothSocket socket) {
					Log.i(TAG, "ConnectedThread");
					mmSocket = socket;
					InputStream tmpIn = null;
					OutputStream tmpOut = null;
		 
					try {
						tmpIn = socket.getInputStream();
						tmpOut = socket.getOutputStream();
					} catch (IOException e) {
					}
		 
					// データ受信用
					mmInStream = tmpIn;
					// データ送信用
					mmOutStream = tmpOut;
				}
		 
				public void run() {
					Log.i(TAG, "ConnectionThread#run()");
					byte[] buffer = new byte[1024];
					int bytes;
		 
					// Whileループで入力が入ってくるのを常時待機
					while (true) {
						try {
							// InputStreamから値を取得
							bytes = mmInStream.read(buffer);
							// 取得したデータをStringの変換
							final String readMsg = new String(buffer, 0, bytes, "UTF-8");
							// Logに表示
							Log.d(TAG, "GET: " + readMsg);
							//TextViewに表示
							//メインスレッドのメッセージキューにメッセージを登録します。
				            mHandler.post(new Runnable() {
				                //run()の中の処理はメインスレッドで動作されます。
				                public void run() {
				                    //画面のtextViewに結果を表示させる。
				                	yourscore.setText("あいつのスコア:"+readMsg);
				                	if(Integer.valueOf(readMsg)>ipoint){
				                		TextView winner=(TextView)findViewById(R.id.textwin);
				                		winner.setText("あいつの勝ち！");
				                	}else if(Integer.valueOf(readMsg)<ipoint){
				                		TextView winner=(TextView)findViewById(R.id.textwin);
				                		winner.setText("俺の勝ち！");
				                	}else{
				                		TextView winner=(TextView)findViewById(R.id.textwin);
				                		winner.setText("引き分け！");
				                	}
				                }
				            });
							
						} catch (IOException e) {
							break;
						}
					}
				}
		 
				/**
				 * 書き込み処理
				 */
				public void write(byte[] bytes) {
					try {
						mmOutStream.write(bytes);
					} catch (IOException e) {
					}
				}
		 
				/**
				 * キャンセル時に呼ばれる
				 */
				public void cancel() {
					try {
						mmSocket.close();
					} catch (IOException e) {
					}
				}
			}
		 
		}