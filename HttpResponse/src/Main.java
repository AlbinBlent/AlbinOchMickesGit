import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		RequestHttp request = new RequestHttp();
		long pingTime;
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PrintWriter writer = null;
	try {
		writer = new PrintWriter("PC-Ping.csv", "UTF-8");
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		try {
			for (int i = 0; i < 5; i++) {

				pingTime = request.getResponse();
				String timeStamp = s.format(new Date());

			
				writer.println(timeStamp + "," + String.valueOf(pingTime));

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}
}