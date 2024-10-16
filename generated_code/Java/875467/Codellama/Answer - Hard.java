import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.io.*;
import java.security.cert.X509Certificate;

public class HttpsConnection {
    public static void main(String[] args) throws Exception {
        // Load the keystore password from an environment variable
        String keystorePassword = System.getenv("KEYSTORE_PASSWORD");

        if (keystorePassword == null || keystorePassword.isEmpty()) {
            throw new IllegalArgumentException("Keystore password not found in environment variables.");
        }

        // Set up the client certificate and keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("/path/to/keystore.jks"), keystorePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keystorePassword.toCharArray());

        // Use the default TrustManagerFactory to get the default TrustManagers
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore)null); // Initialize with null to use default trust store (e.g., the default system CA certificates)

        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());

        // Set up the URL and create the connection
        URL url = new URL("https://somehost.dk:3049");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());

        // Set up the input stream and read the response
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
