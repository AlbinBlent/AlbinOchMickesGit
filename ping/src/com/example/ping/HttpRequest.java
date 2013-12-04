package com.example.ping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpRequest {

	String host;

	public long getHttpResponseTime() {
		long pingTime = 0;

		try {

			final long startTime = System.currentTimeMillis();
			URL url = new URL(host);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestMethod("GET");
			
			urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
			urlc.connect();
			
			if (urlc.getResponseCode() == 200) {
				Log.i("connect", "getResponseCode == 200");
				final long endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;
				urlc.disconnect();
				
			} else {
				System.out.println(urlc.getResponseCode());

				System.out.println(urlc.getResponseMessage());
				final long endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;
				urlc.disconnect();
				
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pingTime;
	}

	public void setHost(String host) {
		this.host = host;

	}
}
