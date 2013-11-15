import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings("deprecation")
public class RequestHttp {

	HttpClient httpclient = new DefaultHttpClient();
	String responseString = null;
	long pingTime;
	long startTime = 0;
	long endTime = 0;
	HttpResponse response;

	public long getResponse() {
		pingTime = 0;
		try {
			startTime = System.currentTimeMillis();
			response = httpclient.execute(new HttpGet("http://dn.se"));
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

				endTime = System.currentTimeMillis();
				pingTime = endTime - startTime;
				System.out.println("time: " + pingTime + " ms. Status code: "
						+ statusLine.getStatusCode());

			} else {
				System.out.println("gick inte");
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
		} catch (IOException f) {
			// TODO Handle problems..
		}
		return pingTime;
	}
}
