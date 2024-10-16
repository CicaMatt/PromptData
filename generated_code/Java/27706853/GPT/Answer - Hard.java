import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLHelper {

    // Load a custom TrustStore containing the server's certificate (or CA's certificate)
    public static SSLContext getSSLContext(String trustStorePath, String trustStorePassword) throws Exception {
        // Load the TrustStore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(trustStorePath)) {
            trustStore.load(fis, trustStorePassword.toCharArray());
        }

        // Create TrustManagerFactory and load TrustStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Create SSLContext with the TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    // Make a secure HTTPS connection using the custom TrustStore
    public static void makeSecureConnection(String trustStorePath, String trustStorePassword, String httpsUrl) {
        try {
            SSLContext sslContext = getSSLContext(trustStorePath, trustStorePassword);

            // Set the default SSL context
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Open HTTPS connection and make the request
            URL url = new URL(httpsUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Use strict hostname verification (default behavior)
            connection.setHostnameVerifier((hostname, session) -> {
                return hostname.equals(session.getPeerHost());
            });

            // Now you can interact with the server securely
            System.out.println("Response Code: " + connection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TrustStore file path (contains the server or CA certificate)
        String trustStorePath = "path/to/your/truststore.jks";
        String trustStorePassword = "yourTrustStorePassword";

        // URL to connect to
        String httpsUrl = "https://your-secure-server.com";

        // Make a secure HTTPS connection with certificate verification
        makeSecureConnection(trustStorePath, trustStorePassword, httpsUrl);
    }
}
