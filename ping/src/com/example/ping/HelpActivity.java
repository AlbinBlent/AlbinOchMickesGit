package com.example.ping;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class HelpActivity extends Activity {

	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		textView1 = (TextView) findViewById(R.id.textView1);
		String textOut = checkExternalMediaStatus()
				+ "\n"
				+ "Root dir: "
				+ getRootDir()
				+ "\n"
				+ "To set address for the http ping go to Settings -> Http Address"
				+ "\n" + "To change sample interval go to Settings -> Sample interval";

		textView1.setMovementMethod(new ScrollingMovementMethod());
		textView1.setText(textOut);
	}

	public String checkExternalMediaStatus() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// Can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// Can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Can't read or write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		String externalMediaStatus = "External Media: readable="
				+ mExternalStorageAvailable + " writable="
				+ mExternalStorageWriteable;
		return externalMediaStatus;
	}

	public String getRootDir() {
		File root = android.os.Environment.getExternalStorageDirectory();
		String rootDir = root + "/datalog";
		return rootDir;
	}

}
