import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpsClientWithClientCert {
    public static void main(String[] args) {
        try {
            // Load the client certificate (PKCS#12 format)
            String clientCertPath = "/path/to/client-cert.p12";
            String clientCertPassword = "your-client-cert-password";
            
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(clientCertPath)) {
                keyStore.load(fis, clientCertPassword.toCharArray());
            }

            // Load the trust store with the server's root certificate (JKS format)
            String trustStorePath = "/path/to/truststore.jks";
            String trustStorePassword = "truststore-password";

            KeyStore trustStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(trustStorePath)) {
                trustStore.load(fis, trustStorePassword.toCharArray());
            }

            // Initialize KeyManagerFactory with the client certificate keystore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, clientCertPassword.toCharArray());

            // Initialize TrustManagerFactory with the trust store
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Initialize SSLContext with both key and trust managers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            // Create the HttpsURLConnection using the custom SSLSocketFactory
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            // Optional: configure timeouts
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // Get the response from the server
            try (InputStream inputStream = conn.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
