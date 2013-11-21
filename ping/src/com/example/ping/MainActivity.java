package com.example.ping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
					updateView(collectData());
				}
			});
		}
	}

	/* Called when the application is minimized */// KANSKE VI INTE VILL HA DÅ
													// VI VILL SAMLA DATA HELA
													// TIDEN
													// @Override
//	 protected void onPause() {														SKRIV DESSA METODER I PHONEINFO CLASSEN!!!
//	 super.onPause();
//	 phoneInfo.getTM().listen(signalStrengthListener, PhoneStateListener.LISTEN_NONE);
//	 }
//
//	/* Called when the application resumes */// KANSKE VI INTE VILL HA DÅ VI
//												// VILL SAMLA DATA HELA TIDEN
//												// @Override
//	 protected void onResume() {												SKRIV DESSA METODER I PHONEINFO CLASSEN!!!
//	 super.onResume();
//	 phoneInfo.getTM().listen(signalStrengthListener,
//	 PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
//	 }

	public void startButton(View view) {

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
		// String cellID = Integer.toHexString(phoneInfo.getCID());
		int cellID = phoneInfo.getCID();
		String netType = phoneInfo.getNetType();

		String collectedData = "time: " + timeStamp + " IP: " + hostOut
				+ " ms: " + httpPingOut + " dBm: " + dbm + " cellID: " + cellID
				+ " NetType: " + netType + "\n ";

		return collectedData;
	}

	public void updateView(String out) {
		textView1.setMovementMethod(new ScrollingMovementMethod());
		textView1.append(out);
	}
}