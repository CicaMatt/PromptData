import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

public class SecureSSLConnection {

    public static void main(String[] args) throws Exception {
        // URL to connect to
        URL url = new URL("https://www.google.com");

        // Open a secure HTTPS connection
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // Optionally set a timeout
        con.setConnectTimeout(5000); 
        con.setReadTimeout(5000);    

        // The certificate validation happens automatically here with Java's default trust store.
        // There's no need to manually trust certificates or disable verification.

        // Check the response code to verify the success of the connection
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            // Successfully connected, now read the response
            try (Reader reader = new InputStreamReader(con.getInputStream());
                 BufferedReader br = new BufferedReader(reader)) {

                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } else {
            System.out.println("Failed to connect. HTTP response code: " + responseCode);
        }
    }
}
