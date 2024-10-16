import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpsClient {

    public static void main(String[] args) {

        try {
            // 1. Load Keystore and Truststore
            KeyStore keyStore = KeyStore.getInstance("JKS"); // Assuming JKS format
            keyStore.load(new FileInputStream("/path/to/your/keystore.jks"), "your_keystore_password".toCharArray());

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream("/path/to/your/truststore.jks"), "your_truststore_password".toCharArray());

            // 2. Initialize KeyManager and TrustManager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "your_client_certificate_password".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            // 3. Create SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // 4. Create HttpsURLConnection
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            // 5. Connect and Read Response
            InputStream inputstream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}