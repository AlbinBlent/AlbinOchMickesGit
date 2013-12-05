package com.example.JavaPing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class main {

	static String host;
	static long sampleIntervalLong;
	static MyTimerTask myCollectorTask;
	static Timer myCollectorTimer;
	static PrintWriter writer;
	static SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd#HH-mm-ss");
	static String responseCode = "";

	public static void main(String[] args) {

		host = "http://httpbin.org/status/200";
		sampleIntervalLong = 1000;
		String dir = "U:\\datalog\\";

		myCollectorTask = new MyTimerTask();
		myCollectorTimer = new Timer();
		myCollectorTimer.schedule(myCollectorTask, 0, sampleIntervalLong);

		String fileName = s.format(new Date()) + "#TemPing.csv";

		File file = new File(dir + fileName);
		file.getParentFile().mkdirs();
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class MyTimerTask extends TimerTask { // FIXA SKIRIV TILL FIL
		public void run() {
			long ping = getHttpResponseTime(host);
			String timeStamp = s.format(new Date());
			String[] parts = timeStamp.split("#");
			String out = parts[0] + "," + parts[1] + "," + ping + "," + host;
			System.out.println("Date: " + parts[0] + " Time: " + parts[1] + " Ping: " + ping + " Host: " + host +" ResponseCode: "+ responseCode);
			writer.println(out);
			writer.flush();
		}
	}

	public static long getHttpResponseTime(String host) {
		long pingTime = 0;

		try {

			final long startTime = System.currentTimeMillis();
			URL url = new URL(host);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestMethod("GET");

			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			urlc.connect();
			int response = urlc.getResponseCode();
			responseCode = String.valueOf(response);
			if (response == 200) {
				final long endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;
				urlc.disconnect();
			} else {
				urlc.disconnect();
			}
		} catch (MalformedURLException e1) {
			responseCode = "Malformed URL Exception";
			e1.printStackTrace();
		} catch (IOException e) {
			responseCode = "IO Exception";
			e.printStackTrace();
		}
		return pingTime;
	}
}
