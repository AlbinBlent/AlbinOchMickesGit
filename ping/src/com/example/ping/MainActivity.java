package com.example.ping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Sampler.Value;
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

	TelephonyManager tm;

	String httpAddress;
	String httpPing;
	MyTimerTask myCollectorTask;
	MyHttpRequestTask myHttpRequestTask;
	Timer myCollectorTimer;
	Timer myHttpRequestTimer;
	TextView textView1;
	TextView textView2;
	HttpRequest httpRequest;
	PhoneInfo phoneInfo;
	LogToFile logObject;
	boolean isStarted;
	boolean pingIsDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		isStarted = false;
		pingIsDone = true;

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		httpRequest = new HttpRequest();
		phoneInfo = new PhoneInfo(getApplicationContext());
		logObject = new LogToFile(getApplicationContext());
		logObject.checkExternalMedia();
	}

	class MyHttpRequestTask extends TimerTask {
		public void run() {
			if (pingIsDone) {
				pingIsDone = false;
				httpPing = String.valueOf(httpRequest.getHttpResponseTime());
				pingIsDone = true;
			} else {
				httpPing = "null";
			}
		}
	}

	class MyTimerTask extends TimerTask {
		public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateView(collectData() + "\n ");
					logObject.writeToFile(collectData());

					
					
					
					/*
					 * Ta med ifall vi vill att varje g�ng vi inte hinner f� en
					 * ny pingtid (pga l�ng pingtid) s� skriver vi ut null
					 * ist�llet f�r den senaste pingtiden.
					 */

					// httpPing = null;
				}
			});
		}
	}

	/* Called when the application is minimized */// KANSKE VI INTE VILL HA D�
													// VI VILL SAMLA DATA HELA
													// TIDEN
													// @Override
	protected void onPause() {
		super.onPause();
		phoneInfo.pausePhoneInfoListner();
	}

	/* Called when the application resumes */// KANSKE VI INTE VILL HA D� VI
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
			Intent settingsActivityIntent = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(settingsActivityIntent);
			Toast.makeText(MainActivity.this, "Settings is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menu_help:
			Intent helpActivityIntent = new Intent(MainActivity.this,
					HelpActivity.class);
			startActivity(helpActivityIntent);
			Toast.makeText(MainActivity.this, "Help is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startButton(View view) {

		if (isStarted) {
			Toast.makeText(MainActivity.this, "Allready running",
					Toast.LENGTH_SHORT).show();
			return;
		}
		/*
		 * Get the preference values from the settings menu.
		 */
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		httpAddress = sharedPref.getString("httpAddress",
				"http://httpbin.org/status/200");
		if (!httpAddress.startsWith("http://")) {
			Toast.makeText(
					MainActivity.this,
					"Invalid internet address \nSet new in Settings -> Http Address",
					Toast.LENGTH_LONG).show();
			return;
		}

		isStarted = true;
		Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();

		textView2.setTextColor(getResources().getColor(R.color.green));
		textView2.setText("Running...");

		/*
		 * Initiate the logger.
		 */
		logObject.writeToSDFile();

		/*
		 * This type cast is very poor but it was way easier than changing the
		 * type of the persistent variable in the NumberPreference class.
		 */
		String sampleIntervalString = sharedPref.getString("sampleInterval",
				"1");
		int sampleIntervalInt = Integer.valueOf(sampleIntervalString);
		sampleIntervalInt = sampleIntervalInt * 1000;
		long sampleIntervalLong = Long.valueOf(sampleIntervalInt);

		/*
		 * Set the host and kick off the tasks.
		 */
		httpRequest.setHost(httpAddress);
		myHttpRequestTask = new MyHttpRequestTask();
		myHttpRequestTimer = new Timer();
		myHttpRequestTimer.schedule(myHttpRequestTask, 0, sampleIntervalLong);

		myCollectorTask = new MyTimerTask();
		myCollectorTimer = new Timer();
		myCollectorTimer.schedule(myCollectorTask, 0, sampleIntervalLong);

	}

	public void stopButton(View view) {
		if (!isStarted) {
			return;
		}
		isStarted = false;
		Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
		logObject.closeOutPutStream();
		textView2.setTextColor(getResources().getColor(R.color.red));
		textView2.setText("Stopped");

		myHttpRequestTask.cancel();
		myHttpRequestTimer.cancel();

		myCollectorTask.cancel();
		myCollectorTimer.cancel();
	}

	public String collectData() {

		String timeStamp = s.format(new Date());
		String hostOut = httpAddress;
		String httpPingOut = httpPing;
		int dbm = phoneInfo.getDBM();
		String cellID = phoneInfo.getCID();
		String lac = phoneInfo.getLac();
		String netType = phoneInfo.getNetType();
		int mcc = phoneInfo.getMCC();
		int mnc = phoneInfo.getMNC();

		String collectedData = timeStamp + "," + dbm + "," + mcc + "," + mnc
				+ "," + lac + "," + cellID + "," + netType + "," + httpPingOut
				+ "," + hostOut;

		return collectedData;
	}

	public void updateView(String out) {
		textView1.setMovementMethod(new ScrollingMovementMethod());
		String[] splitString = out.split(",", 10);
		String hostname = splitString[9].substring(7);
		textView1.setText("Date: " + splitString[0] + "\n" + "Time:"
				+ splitString[1] + "\n" + "dBm: " + splitString[2] + "\n"
				+ "MCC: " + splitString[3] + "\n" + "MNC: " + splitString[4]
				+ "\n" + "Lac: " + splitString[5] + "\n" + "CellID: "
				+ splitString[6] + "\n" + "Net Type: " + splitString[7] + "\n"
				+ "Http Ping: " + splitString[8] + "\n" + "Http Address: "
				+ hostname + "\n" + "Response Code: "
				+ httpRequest.getResponseCode());

	}
}