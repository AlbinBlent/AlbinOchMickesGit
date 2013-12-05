package com.example.ping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpRequest {

	String host;
	String responseCode = "";

	public long getHttpResponseTime() {
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
				Log.i("HttpRequest", "getResponseCode == " + responseCode);
				final long endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;
				urlc.disconnect();
				
			} else {
				Log.i("HttpRequest", "getResponseCode == " + responseCode);
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

	public void setHost(String host) {
		this.host = host;

	}
	public String getResponseCode(){
		return String.valueOf(responseCode);
	}
}
