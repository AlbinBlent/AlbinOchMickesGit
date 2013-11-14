import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Main {

	public static void main(String[] args) {
		HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response;
		String responseString = null;
		long pingTime = 0;
		final long startTime = System.currentTimeMillis();

		try {
			
			response = httpclient.execute(new HttpGet("http://nytimes.se"));
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
				System.out.println("gick inte");
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
		} catch (IOException e) {
			// TODO Handle problems..
		}

	}
}