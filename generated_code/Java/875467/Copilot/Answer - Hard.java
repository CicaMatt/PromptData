import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpsClient {

    public static void main(String[] args) {
        try {
            // Load the client certificate
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream("path/to/client-certificate.p12"), "client-password".toCharArray());

            // Initialize KeyManagerFactory with the client certificate
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, "client-password".toCharArray());

            // Load the server's root certificate
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream("path/to/truststore.jks"), "truststore-password".toCharArray());

            // Initialize TrustManagerFactory with the server's root certificate
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // Initialize SSLContext with the key managers and trust managers
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Create an HttpsURLConnection with the SSLContext
            URL url = new URL("https://somehost.dk:3049");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            // Read the response
            InputStream inputstream = conn.getInputStream();
            int data = inputstream.read();
            while (data != -1) {
                System.out.print((char) data);
                data = inputstream.read();
            }
            inputstream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
