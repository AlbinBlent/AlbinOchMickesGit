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


public class MainActivity extends Activity  {
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static String EXTRA_MESSAGE = "com.example.ping.MESSAGE";

	String host;

	SignalStrengthListener signalStrengthListener;
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

		// start the signal strength listener
		signalStrengthListener = new SignalStrengthListener();
		tm.listen(signalStrengthListener,
				SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

	}
	
	class MyHttpRequestTask extends TimerTask{
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
	@Override
	protected void onPause() {
		super.onPause();
		tm.listen(signalStrengthListener, PhoneStateListener.LISTEN_NONE);
	}

	/* Called when the application resumes */// KANSKE VI INTE VILL HA DÅ VI
												// VILL SAMLA DATA HELA TIDEN
	@Override
	protected void onResume() {
		super.onResume();
		tm.listen(signalStrengthListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	public void startButton(View view) {

		host = editText.getText().toString();
		
		
		httpRequest.setHost(host);
		myHttpRequestTask = new MyHttpRequestTask();
		myHttpRequestTimer = new Timer();
		myHttpRequestTimer.schedule(myHttpRequestTask,0, 50);
		

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

		String collectedData = "time: " + s.format(new Date()) + " IP: " + host
				+ " ms: " + httpPing + " dBm: " + dbm + " cellID: "
				+ getCID(getApplicationContext()) + "\n ";

		return collectedData;
	}

	public void updateView(String out) {
		textView1.setMovementMethod(new ScrollingMovementMethod());
		textView1.append(out);
	}

	public static int getCID(Context ctx) {
		try {
			TelephonyManager tm = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

			// location.getLac(); //ska med

			int locationCellid = location.getCid();
			int cellId = -1; // set to unknown location by default

			if (locationCellid > 0) { // known location
				cellId = locationCellid & 0xffff; // get only valuable bytes
			}
			return cellId;
		} catch (Exception ignored) {
		}

		return -1;
	}

	private class SignalStrengthListener extends PhoneStateListener {
		@Override
		public void onSignalStrengthsChanged(
				android.telephony.SignalStrength signalStrength) {
			// get the signal strength (a value between 0 and 31)
			
				int strengthAmplitude = signalStrength.getGsmSignalStrength();
				dbm = (-113 + strengthAmplitude * 2);
				
			super.onSignalStrengthsChanged(signalStrength);
		}
	}

	public String getNetType(TelephonyManager tm) {
		String NetTypeStr = "unknown";
		switch (tm.getNetworkType()) {
		case 0:
			NetTypeStr = "unknown";
			break;
		case 1:
			NetTypeStr = "GPRS";
			break;
		case 2:
			NetTypeStr = "EDGE";
			break;
		case 3:
			NetTypeStr = "UMTS";
			break;
		case 4:
			NetTypeStr = "CDMA";
			break;
		case 5:
			NetTypeStr = "EVDO_0";
			break;
		case 6:
			NetTypeStr = "EVDO_A";
			break;
		case 7:
			NetTypeStr = "1xRTT";
			break;
		case 8:
			NetTypeStr = "HSDPA";
			break;
		case 9:
			NetTypeStr = "HSUPA";
			break;
		case 10:
			NetTypeStr = "HSPA";
			break;
		case 11:
			NetTypeStr = "iDen";
			break;
		case 12:
			NetTypeStr = "EVDO_B";
			break;
		case 13:
			NetTypeStr = "LTE";
			break;
		case 14:
			NetTypeStr = "eHRPD";
			break;
		case 15:
			NetTypeStr = "HSPA+";
			break;
		}
		return NetTypeStr;
	}
}