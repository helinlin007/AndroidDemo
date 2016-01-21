package com.example.redassistant;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Struct;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.support.v4.app.NotificationCompat;
import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.*;
import com.example.redassistant.Point;

public class MainActivity extends Activity {

	public Button autoGetAssiantButton;//自动抢红包按钮
	public Activity ac = this;
	//获取一个消息通知管理对象
	public NotificationManager mnotificationManager;
	public NotificationCompat.Builder mBuilder;
	public Notification mNotification;
	public boolean mProcessIsStart = false;//判断线程是否已经启动
	
	private String shellArrayString[] = {"input tap 200,300","sleep 1",
										 "input tap 200,300","sleep 1",
										 "input tap 200,300","sleep 1",
										 "input tap 200,300","sleep 1",
										 "input tap 200,300","sleep 1"};
	
	/**
	 *  在程序中，执行shell命令，实现程序的自动化测试
	 * @param command shell数组
	 */
	public void execShell(String commandArray[]) throws IOException {
		Runtime runtime = Runtime.getRuntime();//获取runtime对象
		DataOutputStream osDataOutputStream = null;
		//BufferedReader osBufferedReader = 
		try {
			Process tempProcess = runtime.exec("reboot");// 获得root权限
			
			InputStream inputStream = tempProcess.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			osDataOutputStream = new DataOutputStream(tempProcess.getOutputStream());
			//osDataOutputStream.write(commandArray);
			osDataOutputStream.writeBytes("reboot");
			osDataOutputStream.writeBytes("\n");//
			osDataOutputStream.flush();
//			for (String command : commandArray) {
//				if (commandArray == null) {
//					continue;
//				}
//				//osDataOutputStream.write(command.getBytes());
//				
//				String lineString = "";
//				StringBuilder strBuilder = new StringBuilder(lineString);
//				while ((lineString = bufferedReader.readLine()) != null) {
//					strBuilder.append(lineString);
//					Toast.makeText(ac,lineString,Toast.LENGTH_LONG).show();
//					strBuilder.append("\n");
//				}
//			}
	//		osDataOutputStream.writeBytes("exit\n");
	//		osDataOutputStream.flush();
			tempProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 *   获取当前鼠标点击的坐标值
	 * @return 返回当前操作的坐标
	 */
	public Point getCurrentPos()
	{
		Point figerPoint = new Point();
		figerPoint.setX(50);
		figerPoint.setY(50);
		String posStr = "横坐标为:" + String.valueOf(figerPoint.getX()) +",纵坐标为:" + String.valueOf(figerPoint.getY());
		Toast.makeText(this,posStr,Toast.LENGTH_SHORT).show();
		return figerPoint;
	}
	/**
	 *  模拟手动点击操作
	 * @param currPos 输入当前的坐标值
	 * @return 模拟成功
	 */
	public boolean simulateOnclick(Point currPos) {
		
//		MotionEvent tempMotionEvent = getSystemService(); //定义一个行为对象
//		tempMotionEvent.setLocation((float)currPos.getX(),(float)currPos.getY());
//		tempMotionEvent.setAction(MotionEvent.ACTION_POINTER_DOWN);// 标示手指按下
//		int nCount = tempMotionEvent.getPointerCount();
//		if (tempMotionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN &&
//				Math.abs(tempMotionEvent.getX(0) - currPos.getX()) < 5&&
//				Math.abs(tempMotionEvent.getY(0) - currPos.getY()) < 5) {
//			Toast.makeText(this, "点击了红包", Toast.LENGTH_SHORT);
//		}
		return true;
	}
	/**
	 *  启动一个后台运行的线程，该线程监听通知栏中的消息
	 * @return 启动是否成功
	 */
	public boolean startListenProcess(){
		if(mProcessIsStart)
		{
			return true;	//已经启动了，无需重新启动
		}
		try {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// 监听通知栏的消息
					try {
						while (true) {
							// 给通知栏发送消息功能代码
							mNotification.tickerText = "新建一个消息测试一下";
							mNotification.icon = R.drawable.ic_launcher;
							mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
							mNotification.flags |= Notification.FLAG_NO_CLEAR; 
							mNotification.defaults = Notification.DEFAULT_SOUND;
							
							Intent noIntent = new Intent(getApplicationContext(),ac.getClass());
							PendingIntent contantIntent = 
									PendingIntent.getActivity(getApplicationContext(), 0, noIntent, 0);
							mNotification.setLatestEventInfo(getApplicationContext(), "测试标题", "测试内容", contantIntent);
							mnotificationManager.notify(1, mNotification);
							Thread.sleep(3000);	
						}
						
					} catch (Exception e) {
						
					}
				}
			}).start();
		//	Thread.sleep(100);// 每个0.1s去监听一次
			return true;
			
		} catch (Exception e) {
			//Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	/**
	 *  对当前的控件进行初始化,受不了了，手太快
	 */
	private void InitControl() {
		autoGetAssiantButton = (Button)findViewById(R.id.autoGetRedAssistant);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitControl();
		mnotificationManager  = 
				(NotificationManager)ac.getSystemService(
						Context.NOTIFICATION_SERVICE);
		mBuilder = new
				NotificationCompat.Builder(this);
		mNotification = new Notification();
		autoGetAssiantButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 启动后台监听服务 
				if (startListenProcess()) {
					Toast.makeText(ac, "启动成功，准备抢红包吧",Toast.LENGTH_SHORT).show();
					mProcessIsStart = !mProcessIsStart;
					getCurrentPos();
					if (mProcessIsStart) {
						autoGetAssiantButton.setText("服务中,再点就停止了");
						Runtime runtime = Runtime.getRuntime();
						try {
							Process tempProcess = runtime.exec("reboot");// 获得root权限
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
					else{
						autoGetAssiantButton.setText("启动自动抢红包");
					}
				}
				else {
					Toast.makeText(ac, "启动失败，请重试",Toast.LENGTH_SHORT).show();
				}
			}
		});
		try {
	
			execShell(shellArrayString);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
