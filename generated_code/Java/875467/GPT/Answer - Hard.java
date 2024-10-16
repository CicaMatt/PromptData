import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;

public class SecureHttpsClient {
    
    public static void main(String[] args) {
        try {
            // Define the paths for the keystore (client certificate) and truststore (server certificate)
            String keystorePath = "/path/to/your/keystore.jks";  
            String keystorePassword = "your-keystore-password";  
            String truststorePath = "/path/to/your/truststore.jks";  
            String truststorePassword = "your-truststore-password"; 

            // Load the keystore (client certificate)
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream keyStoreInput = new FileInputStream(keystorePath);
            keyStore.load(keyStoreInput, keystorePassword.toCharArray());
            keyStoreInput.close();

            // Load the truststore (server root certificate)
            KeyStore trustStore = KeyStore.getInstance("JKS");
            FileInputStream trustStoreInput = new FileInputStream(truststorePath);
            trustStore.load(trustStoreInput, truststorePassword.toCharArray());
            trustStoreInput.close();

            // Set up the KeyManagerFactory to use the client certificate from the keystore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

            // Set up the TrustManagerFactory to trust the server certificate from the truststore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Initialize an SSLContext with the KeyManager and TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            // Set the default SSLSocketFactory to the one we just configured
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            
            // Create a URL object for the server
            URL url = new URL("https://somehost.dk:3049");  

            // Open an HTTPS connection to the server
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Set the custom SSLSocketFactory with the client certificate and truststore
            conn.setSSLSocketFactory(sslSocketFactory);

            // Optionally, configure other connection settings
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(10000);  
            conn.setReadTimeout(10000);

            // Read the response from the server
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

            // Clean up
            inputStream.close();
            conn.disconnect();

        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}
