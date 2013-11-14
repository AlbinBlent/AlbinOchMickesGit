package com.example.ping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@SuppressLint("NewApi")
public class MainActivity extends Activity implements AsyncResponse {
	RequestHttp requestHttp = new RequestHttp();
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static String EXTRA_MESSAGE = "com.example.ping.MESSAGE";

	EditText edit;
	TextView text;
	String host;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestHttp.delegate = this;
		 //Get the instance of TelephonyManager  
        TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  
         
        List<CellInfo> cellInfos = (List<CellInfo>) tm.getAllCellInfo();
        
        for(CellInfo cellInfo : cellInfos)
        {
            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;

            CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();

            System.out.println("cellInfoGsm.isRegisterd: " + cellInfoGsm.isRegistered());
            
            System.out.println("cellIdentity: " + cellIdentity.toString());
            
            System.out.println("cellSignalStrengthGsm: " + cellSignalStrengthGsm.toString());
        }
        
        //Calling the methods of TelephonyManager the returns the information  
        String IMEINumber=tm.getDeviceId();  
        String subscriberID=tm.getDeviceId();  
        String SIMSerialNumber=tm.getSimSerialNumber();  
        String networkCountryISO=tm.getNetworkCountryIso();  
        String SIMCountryISO=tm.getSimCountryIso();  
        String softwareVersion=tm.getDeviceSoftwareVersion();  
        String voiceMailNumber=tm.getVoiceMailNumber();  
          
        //Get the phone type  
        String strphoneType="";  
          
        int phoneType=tm.getPhoneType();  
  
        switch (phoneType)   
        {  
                case (TelephonyManager.PHONE_TYPE_CDMA):  
                           strphoneType="CDMA";  
                               break;  
                case (TelephonyManager.PHONE_TYPE_GSM):   
                           strphoneType="GSM";                
                               break;  
                case (TelephonyManager.PHONE_TYPE_NONE):  
                            strphoneType="NONE";                
                                break;  
         }  
          
        //getting information if phone is in roaming  
        boolean isRoaming=tm.isNetworkRoaming();  
          
        String info="Phone Details:\n";  
        info+="\n IMEI Number:"+IMEINumber;  
        info+="\n SubscriberID:"+subscriberID;  
        info+="\n Sim Serial Number:"+SIMSerialNumber;  
        info+="\n Network Country ISO:"+networkCountryISO;  
        info+="\n SIM Country ISO:"+SIMCountryISO;  
        info+="\n Software Version:"+softwareVersion;  
        info+="\n Voice Mail Number:"+voiceMailNumber;  
        info+="\n Phone Network Type:"+strphoneType;  
        info+="\n In Roaming? :"+isRoaming;  
        
        System.out.println(info);
	}

	public void sendPing(View view) {
		// Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.ping_adress);
		host = editText.getText().toString();
		// intent.putExtra(EXTRA_MESSAGE, message);
		// startActivity(intent);
		// new LongOperation().execute(host);
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
		System.out.println("processU: " + output);

		String message = String.valueOf(output);
		
		String format = s.format(new Date());
		
		TextView text = (TextView) findViewById(R.id.textView1);
		text.append("time: " + format + " uppdate IP: " + host + " ms: " + message + "\n ");
	}
}