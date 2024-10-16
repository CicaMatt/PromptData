import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpsClientCertificateExample {

    public static void main(String[] args) {
        try {
            // Load the keystore containing the client certificate and private key
            KeyStore keyStore = KeyStore.getInstance("JKS"); 
            keyStore.load(new FileInputStream("/path/to/your/keystore.jks"), "your_keystore_password".toCharArray());

            // Load the truststore containing the server's self-signed certificate
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("/path/to/your/truststore.jks"), "your_truststore_password".toCharArray());

            // Create a key manager factory to manage the client certificate and private key
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "your_client_certificate_password".toCharArray());

            // Create a trust manager factory to manage the server's self-signed certificate
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Create an SSL context and initialize it with the key and trust managers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // Create an HttpsURLConnection and set the SSL socket factory
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            // Get the input stream and process the response
            InputStream inputstream = conn.getInputStream();
            // ... process the inputstream
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}