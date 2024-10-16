import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class SecureSSLConnection {

    public static void main(String[] args) {
        try {
            BufferedReader br = null;
            // Define the HTTPS URL
            URL url = new URL("https://www.google.com");

            // Create a connection object
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            // Optionally, customize SSLContext for better control
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Initialize the default TrustManager using default system keystore (this ensures certificate validation)
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // Load default keystore (comes with Java)
            ks.load(null, null); 
            tmf.init(ks);

            // Set up the SSLContext to use the default trust managers (which validate the certificates)
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            // Set the SSLContext to be used by this connection
            con.setSSLSocketFactory(sslContext.getSocketFactory());

            // Set a reasonable timeout for connection and read operations
            con.setConnectTimeout(5000); 
            con.setReadTimeout(5000); 

            // Optional: Verify the certificate and hostname by default (no need for manual HostnameVerifier)

            // Open connection and read the input stream
            Reader reader = new InputStreamReader(con.getInputStream());
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            // Handle exceptions appropriately in production code
            e.printStackTrace(); 
        } finally {
            // Ensure resources are closed properly
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } 
}
