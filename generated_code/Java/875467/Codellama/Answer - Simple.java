import java.io.FileInputStream;
import java.net.URL;
import javax.net.ssl.*;
import java.security.KeyStore;

public class HttpsClient {
    public static void main(String[] args) throws Exception {
        // Load the client certificate and private key from a file
        KeyStore clientKeystore = KeyStore.getInstance("JKS");
        FileInputStream clientCertificateFile = new FileInputStream("client_cert.jks");
        clientKeystore.load(clientCertificateFile, "password".toCharArray());

        // Load the trusted root certificate from a file
        KeyStore truststore = KeyStore.getInstance("JKS");
        FileInputStream rootCertificateFile = new FileInputStream("root_cert.jks");
        truststore.load(rootCertificateFile, "password".toCharArray());

        // Create an SSL socket factory using the client certificate and private key, and the trusted root certificate
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(clientKeystore, truststore);

        // Create a URL object for the HTTPS connection
        URL url = new URL("https://somehost.dk:3049");

        // Open an HTTPS connection using the SSL socket factory and the URL object
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(sslSocketFactory);

        // Get the input stream from the HTTPS connection
        InputStream inputstream = conn.getInputStream();

        // Read data from the input stream and process it
        while ((inputstream.read()) != -1) {
            System.out.print((char)inputstream.read());
        }
    }

    private static SSLSocketFactory getSSLSocketFactory(KeyStore clientKeystore, KeyStore truststore) throws Exception {
        // Create a TrustManager that trusts the CAs in the truststore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
        tmf.init(truststore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        // Create an SSLContext that uses the TrustManager and the client certificate
        SSLContext sslcontext = SSLContext.getInstance("TLS", "SunJSSE");
        sslcontext.init(null, trustManagers, new SecureRandom());

        // Return the SSL context as a socket factory
        return sslcontext.getSocketFactory();
    }
}