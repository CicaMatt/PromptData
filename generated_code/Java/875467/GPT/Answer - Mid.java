import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

public class HttpsClientCertificateExample {

    public static void main(String[] args) {
        try {
            // Load the client keystore where client certificate is stored
            String clientKeystorePath = "/path/to/client-keystore.jks";
            String clientKeystorePassword = "keystorePassword";
            String truststorePath = "/path/to/truststore.jks";
            String truststorePassword = "truststorePassword";

            // Create KeyStore instance for the client certificate
            KeyStore clientKeyStore = KeyStore.getInstance("JKS");
            FileInputStream keyStoreInputStream = new FileInputStream(clientKeystorePath);
            clientKeyStore.load(keyStoreInputStream, clientKeystorePassword.toCharArray());
            keyStoreInputStream.close();

            // Create KeyManagerFactory to use client certificate
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(clientKeyStore, clientKeystorePassword.toCharArray());

            // Load the truststore that contains the server's self-signed root certificate
            KeyStore trustStore = KeyStore.getInstance("JKS");
            FileInputStream trustStoreInputStream = new FileInputStream(truststorePath);
            trustStore.load(trustStoreInputStream, truststorePassword.toCharArray());
            trustStoreInputStream.close();

            // Create TrustManagerFactory to validate the server's certificate
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            // Initialize SSL context with the client certificate and the trusted root certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // Set the default SSLSocketFactory to the one created from the above context
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create an HTTPS connection to the remote server
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslSocketFactory);
            conn.setRequestMethod("GET");

            // Optional: set connection timeout
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // Send the request and read the response
            InputStream inputStream = conn.getInputStream();
            int data;
            while ((data = inputStream.read()) != -1) {
                System.out.print((char) data);
            }
            inputStream.close();

            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
