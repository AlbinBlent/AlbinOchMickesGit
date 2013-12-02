package com.example.JavaPing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class main {

	static String host;
	static long sampleIntervalLong;
	static MyTimerTask myCollectorTask;
	static Timer myCollectorTimer;
	static PrintWriter writer;
	static SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd#HH-mm-ss");

	public static void main(String[] args) {		

		host = "http://dn.se";
		sampleIntervalLong = 1000;
		String dir = "U:\\datalog\\";

		myCollectorTask = new MyTimerTask();
		myCollectorTimer = new Timer();
		myCollectorTimer.schedule(myCollectorTask, 0, sampleIntervalLong);

		// myCollectorTask.cancel();
		// myCollectorTimer.cancel();
		String fileName = s.format(new Date()) + "#TemPing.csv";
//		fileName = "hej.csv";
		File file = new File(dir + fileName);
		file.getParentFile().mkdirs();
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		writer.println("date,time,responeTime,host");
		System.out.println("date,time,responeTime,host");
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

	@SuppressWarnings("unused")
	public static long getHttpResponseTime(String host) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response;
		String responseString = null;
		long pingTime = 0;

		final long startTime = System.currentTimeMillis();
		try {
			response = httpclient.execute(new HttpGet(host));
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

				final long endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
		} catch (IOException e) {
			// TODO Handle problems..
		}

		return pingTime;
	}
}
