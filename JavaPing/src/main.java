import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class main {
	
	static String host;
	static long sampleIntervalLong;
	static MyTimerTask myCollectorTask;
	static Timer myCollectorTimer;

	public static void main(String[] args) {
		
		
		host = "http://dn.se";
		sampleIntervalLong = 1000;
		
		myCollectorTask = new MyTimerTask();
		myCollectorTimer = new Timer();
		myCollectorTimer.schedule(myCollectorTask, 0, sampleIntervalLong);
		
//		myCollectorTask.cancel();
//		myCollectorTimer.cancel();
    }
	
	public static class MyTimerTask extends TimerTask {							//FIXA SKIRIV TILL FIL
		public void run() {
			long ping = getHttpResponseTime(host);
			
			System.out.println("Host: " + host + " ping: " + ping);
		}
	}
	
	public static long getHttpResponseTime(String host) {
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
	public void setHost(String host){
//		this.host = host;
		
	}
	

}

