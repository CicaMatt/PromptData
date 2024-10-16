import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class SecureHttpsClient {

    public static void main(String[] args) {
        try {
            // 1. Load Keystore and Truststore
            KeyStore keyStore = KeyStore.getInstance("PKCS12"); 
            keyStore.load(
                new FileInputStream("/path/to/your/client.p12"), "your_client_certificate_password".toCharArray());

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(
                new FileInputStream("/path/to/your/truststore.jks"), "your_truststore_password".toCharArray()); 

            // 2. Initialize KeyManager and TrustManager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.
                getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "your_client_certificate_password".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // 3. Create SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS"); 
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // 4. Set Custom SSLSocketFactory
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // 5. Establish HTTPS Connection
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // 6. Handle Response
            InputStream inputstream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace(); 
        }
    }
}