package com.example.ping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
	public final static String EXTRA_MESSAGE = "com.example.ping.MESSAGE";

	String host;

	// SignalStrengthListener signalStrengthListener;
	TelephonyManager tm;

	String httpPing;
	int dbm;
	int interval;
	EditText editText;
	MyTimerTask myCollectorTask;
	MyHttpRequestTask myHttpRequestTask;
	Timer myCollectorTimer;
	Timer myHttpRequestTimer;
	TextView textView1;
	HttpRequest httpRequest;
	PhoneInfo phoneInfo;
	LogToFile logObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.ping_adress);
		interval = 1000;
		textView1 = (TextView) findViewById(R.id.textView1);

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		httpRequest = new HttpRequest();
		phoneInfo = new PhoneInfo(getApplicationContext());
		logObject = new LogToFile(getApplicationContext());
		logObject.checkExternalMedia();

		// start the signal strength listener
		// signalStrengthListener = new SignalStrengthListener();
		// tm.listen(signalStrengthListener,
		// SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

	}

	class MyHttpRequestTask extends TimerTask {
		public void run() {

			httpPing = String.valueOf(httpRequest.getHttpResponseTime());
		}
	}

	class MyTimerTask extends TimerTask {
		public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateView(collectData() + "\n ");
					logObject.writeToFile(collectData());
				}
			});
		}
	}

	/* Called when the application is minimized */// KANSKE VI INTE VILL HA DÅ
													// VI VILL SAMLA DATA HELA
													// TIDEN
													// @Override
	protected void onPause() {
		super.onPause();
		phoneInfo.pausePhoneInfoListner();
	}

	/* Called when the application resumes */// KANSKE VI INTE VILL HA DÅ VI
												// VILL SAMLA DATA HELA TIDEN
												// @Override
	protected void onResume() {
		super.onResume();
		phoneInfo.resumePhoneInfoListner();
	}

	// Initiating Menu XML file (menu.xml)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent k = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(k);
			Toast.makeText(MainActivity.this, "Settings is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menu_help:
			// Intent k = new Intent(MainActivity.this, SettingsActivity.class);
			// startActivity(k);
			Toast.makeText(MainActivity.this, "Help is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startButton(View view) {

		logObject.writeToSDFile();

		host = editText.getText().toString();

		httpRequest.setHost(host);
		myHttpRequestTask = new MyHttpRequestTask();
		myHttpRequestTimer = new Timer();
		myHttpRequestTimer.schedule(myHttpRequestTask, 0, 50);

		myCollectorTask = new MyTimerTask();
		myCollectorTimer = new Timer();
		myCollectorTimer.schedule(myCollectorTask, 0, interval);

	}

	public void stopButton(View view) {
		logObject.closeOutPutStream();

		myHttpRequestTask.cancel();
		myHttpRequestTimer.cancel();

		myCollectorTask.cancel();
		myCollectorTimer.cancel();
	}

	public String collectData() {

		String timeStamp = s.format(new Date());
		String hostOut = host;
		String httpPingOut = httpPing;
		int dbm = phoneInfo.getDBM();
		String cellID = phoneInfo.getCID();
		String lac = phoneInfo.getLac();
		String phoneType = phoneInfo.getPhoneType();
		String netType = phoneInfo.getNetType();
		int mcc = phoneInfo.getMCC();
		int mnc = phoneInfo.getMNC();

		String collectedData = timeStamp + "," + phoneType + "," + dbm + ","
				+ mcc + "," + mnc + "," + lac + "," + cellID + "," + netType
				+ "," + httpPingOut + "," + hostOut;

		return collectedData;
	}

	public void updateView(String out) {
		String oldText = textView1.getText().toString();
		textView1.setMovementMethod(new ScrollingMovementMethod());
		textView1.setText(out + oldText);
	}
}