import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

public class HttpsClient {
    public static void main(String[] args) {
        try {
            // Load the client certificate into a KeyStore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("/path/to/your/clientkeystore.jks"), "keystorepassword".toCharArray());

            // Initialize a KeyManagerFactory with the KeyStore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "keypassword".toCharArray());

            // Load the server's root certificate into a TrustStore
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("/path/to/your/truststore.jks"), "truststorepassword".toCharArray());

            // Initialize a TrustManagerFactory with the TrustStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Initialize an SSLContext with the KeyManagers and TrustManagers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // Set the default SSLSocketFactory to use the SSLContext
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create an HttpsURLConnection with the custom SSLSocketFactory
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslSocketFactory);

            // Read the response
            InputStream inputStream = conn.getInputStream();
            // Process the input stream as needed
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
