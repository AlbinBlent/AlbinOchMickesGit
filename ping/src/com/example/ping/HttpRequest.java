package com.example.ping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.widget.Toast;

public class HttpRequest {
	
	String host;

	public long getHttpResponseTime() {
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
				System.out.println("http: " + pingTime);

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
	public void setHost(String host){		
		this.host = host;
		
	}
}
