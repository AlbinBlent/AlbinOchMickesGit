package com.example.ping;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


@SuppressLint("NewApi")
public class MainActivity extends Activity implements AsyncResponse {
	RequestHttp requestHttp = new RequestHttp();
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static String EXTRA_MESSAGE = "com.example.ping.MESSAGE";

	EditText edit;
	TextView text;
	String host;

	SignalStrengthListener signalStrengthListener;
	TelephonyManager tm;
	
	String httpPing;
	int dbm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestHttp.delegate = this;
		
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		// start the signal strength listener
		signalStrengthListener = new SignalStrengthListener();
		tm.listen(signalStrengthListener,
				SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

		int cellIDny = getCID(getApplicationContext()); // FUNKAR BRA
		System.out.println("cellIDny: " + cellIDny);
	}

	/* Called when the application is minimized */										//KANSKE VI INTE VILL HA DÅ VI VILL SAMLA DATA HELA TIDEN
	@Override
	protected void onPause() {
		super.onPause();
		tm.listen(signalStrengthListener, PhoneStateListener.LISTEN_NONE);
	}

	/* Called when the application resumes */											//KANSKE VI INTE VILL HA DÅ VI VILL SAMLA DATA HELA TIDEN
	@Override
	protected void onResume() {
		super.onResume();
		tm.listen(signalStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	public void start(View view) {
		EditText editText = (EditText) findViewById(R.id.ping_adress);
		host = editText.getText().toString();
		requestHttp.execute(host);
		
	}

	@Override
	public void processFinish(Long output) {			
		// TODO Auto-generated method stub
		System.out.println("processF: " + output);

		String message = String.valueOf(output);

		System.out.println("result " + message);

		TextView text = (TextView) findViewById(R.id.textView1);
		text.append("IP: " + host + " ms: " + message + "\n ");

	}

	@Override
	public void processUppdate(Long output) {
		// TODO Auto-generated method stub

		String message = String.valueOf(output);
		httpPing = message;

//		TextView text = (TextView) findViewById(R.id.textView1);
//		text.append("time: " + httpPing + " uppdate IP: " + host + " ms: "
//				+ message + "\n ");
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

			System.out.println("netType: " + getNetType(tm));

			// get the signal strength (a value between 0 and 31)
			switch (tm.getNetworkType()) {
			case 0:
				// NetTypeStr = "unknown";

				break;
			case 1:
				// NetTypeStr = "GPRS";

			case 2:
				// NetTypeStr = "EDGE";

			case 3:
				// NetTypeStr = "UMTS";
				int strengthAmplitude = signalStrength.getGsmSignalStrength();
				dbm = (-113 + strengthAmplitude * 2);
				break;
			case 4:
				// NetTypeStr = "CDMA";
				dbm = signalStrength.getCdmaDbm();
				break;
			case 5:
				// NetTypeStr = "EVDO_0";
				break;
			case 6:
				// NetTypeStr = "EVDO_A";
				break;
			case 7:
				// NetTypeStr = "1xRTT";
				break;
			case 8:
				// NetTypeStr = "HSDPA";
				dbm = signalStrength.getCdmaDbm();
				break;
			case 9:
				// NetTypeStr = "HSUPA";
				dbm = signalStrength.getCdmaDbm();
				break;
			case 10:
				// NetTypeStr = "HSPA";
				dbm = signalStrength.getCdmaDbm();
				break;
			case 11:
				// NetTypeStr = "iDen";
				break;
			case 12:
				// NetTypeStr = "EVDO_B";
				break;
			case 13:
				// NetTypeStr = "LTE";
				break;
			case 14:
				// NetTypeStr = "eHRPD";
				break;
			case 15:
				// NetTypeStr = "HSPA+";
				dbm = signalStrength.getCdmaDbm();
				
				break;
			}
			// do something with it (in this case we update a text view)
			System.out.println("Dbm: " + dbm);
			super.onSignalStrengthsChanged(signalStrength);

			TextView text = (TextView) findViewById(R.id.textView1);
			text.append("dbm: " + dbm + "\n ");
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
	public void collectAndStoreValuse(int samples,int interval){
		for (int i = 0; i < samples; i++) {
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String collectedData = "time: " + s.format(new Date()) + " IP: " + host + " ms: "
					+ httpPing + " dBm: " + dbm + " cellID: " + getCID(getApplicationContext()) + "\n ";
			TextView text = (TextView) findViewById(R.id.textView1);
			text.append(collectedData);
		}
	}

}