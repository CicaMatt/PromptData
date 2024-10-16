import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;

public class SecureHttpPost {

    public static void main(String[] args) {
        try {
            // Load the server certificate from a file or a keystore
            FileInputStream fis = new FileInputStream("path/to/your/truststore.jks");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(fis, "your-keystore-password".toCharArray());

            // Create TrustManagerFactory and load the truststore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // Initialize SSLContext with the trusted certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Set the default SSLContext to be used by all HTTPS connections
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Create the HTTPS connection
            URL url = new URL("https://yourserver.com/api/resource");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Set request properties like headers, etc.
            conn.setRequestProperty("Content-Type", "application/json");
            
            // Prepare the payload
            String payload = "{\"key\":\"value\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response
            try (InputStream is = conn.getInputStream()) {
                byte[] response = is.readAllBytes();
                System.out.println(new String(response, "utf-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
