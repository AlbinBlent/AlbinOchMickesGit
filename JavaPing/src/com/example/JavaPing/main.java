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
			writer.println(out);
			System.out.println(out);
			writer.flush();
		}
	}

	public static long getHttpResponseTime(String host) {
		long pingTime = 0;

		try {

			
			URL url = new URL(host);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestMethod("GET");

			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			final long startTime = System.currentTimeMillis();
			urlc.connect();

			System.out.println(urlc.getResponseCode());
			final long endTime = System.currentTimeMillis();
			pingTime = endTime - startTime;
			urlc.disconnect();

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pingTime;
	}
}
