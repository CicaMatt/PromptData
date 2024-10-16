import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class ODKHttpsConnection {

    public static void main(String[] args) {
        try {
            // Load the system's default truststore
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            // Create a TrustManager that validates certificates against the truststore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // Create an SSLContext that uses the TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS"); 
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Set the default SSLSocketFactory for all HttpsURLConnections
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Now you can access an https URL with certificate verification
            URL url = new URL("https://your-localhost-url");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            // ... rest of your code to interact with the connection
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | MalformedURLException | IOException e) {
            e.printStackTrace();
        } 
    }
}