package com.example.ping;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class PhoneInfo {
	TelephonyManager tm;
	SignalStrengthListener signalStrengthListener;
	int dbm;
	Context ctx;

	public PhoneInfo(Context ctx) {
		this.ctx = ctx;
		tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		signalStrengthListener = new SignalStrengthListener();
		tm.listen(signalStrengthListener,
				SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);

	}
	
	public TelephonyManager getTM(){
		return tm;
	}

	public int getCID() {
		try {

			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

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

	public int getLac() {
		try {

			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

			int locationLac = location.getLac();
			int lac = -1; // set to unknown location by default

			if (locationLac > 0) { // known location
				lac = locationLac & 0xffff; // get only valuable bytes
			}
			return lac;
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
	
	public int getDBM(){
		return dbm;
	}

	public String getNetType() {
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
