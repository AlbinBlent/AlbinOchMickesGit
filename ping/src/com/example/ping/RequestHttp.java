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

import android.os.AsyncTask;
import android.widget.TextView;

class RequestHttp extends AsyncTask<String, Long, Long> {
	public AsyncResponse delegate = null;

	@Override
	protected Long doInBackground(String... uri) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response;
		String responseString = null;
		long pingTime = 0;

		for (int i = 0; i < 10; i++) {

			final long startTime = System.currentTimeMillis();
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
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
			publishProgress(pingTime);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return pingTime;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		delegate.processUppdate(values[0]);
	}

	@Override
	protected void onPostExecute(Long result) {
		super.onPostExecute(result);
		delegate.processFinish(result);

		// Do anything with response..
	}
}
