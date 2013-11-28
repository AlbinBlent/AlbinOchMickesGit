package com.example.ping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class LogToFile {

	Context context;
	PrintWriter printwriter;
	FileOutputStream fileoutputstream;
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss");

	FileOutputStream outputStreamSecondFkn;

	public LogToFile(Context context) {
		this.context = context;
	}

	public void checkExternalMedia() {
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
		System.out.println("\n\nExternal Media: readable="
				+ mExternalStorageAvailable + " writable="
				+ mExternalStorageWriteable);
	}

	/**
	 * Method to write ascii text characters to file on SD card. Note that you
	 * must add a WRITE_EXTERNAL_STORAGE permission to the manifest file or this
	 * method will throw a FileNotFound Exception because you won't have write
	 * permission.
	 */

	public void writeToSDFile() {

		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-
		// storage.html#filesExternal

		File root = android.os.Environment.getExternalStorageDirectory();

		System.out.println("\nExternal file system root: " + root);

		// See
		// http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		File dir = new File(root.getAbsolutePath() + "/datalog");
		dir.mkdirs();
		String fileName = s.format(new Date()) + "#PhoneDataLog.csv";
		File file = new File(dir, fileName);

		// if(!file.exists()){ För att använda internal storage
		// String filename = "myfile";
		//
		// try {
		// outputStreamSecondFkn = context.openFileOutput(filename,
		// Context.MODE_PRIVATE);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		System.out.println("File: " + file);

		String TAG = "LogToFile";

		try {
			fileoutputstream = new FileOutputStream(file);
			printwriter = new PrintWriter(fileoutputstream);
			printwriter
					.print("date,time,phoneType,dBm,mcc,mnc,lac,cellID,netType,responeTime,host"
							+ "\r\n");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i(TAG, "******* File not found.");
		}
		System.out.println("\n\nFile written to " + file);
	}

	public void writeToFile(String message) {
		printwriter.print(message + "\r\n");

		// try { För att använda internal storage
		// outputStreamSecondFkn.write(message.getBytes());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void closeOutPutStream() {
		printwriter.flush();
		printwriter.close();
		try {
			fileoutputstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try { För att använda internal storage
		// outputStreamSecondFkn.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
