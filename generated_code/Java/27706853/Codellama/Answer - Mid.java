import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Registry;
import org.apache.http.conn.scheme.RegistryBuilder;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SecureSSLConnection {

    public static void main(String[] args) {
        try {
            // Load the self-signed certificate
            FileInputStream certInputStream = new FileInputStream("path/to/your/self-signed-cert.crt");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);

            // Create a KeyStore containing the trusted certificate
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null); 
            trustStore.setCertificateEntry("localhost", certificate); 

            // Create an SSLContext that uses the custom trust store
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, null)  
                    .build();

            // Create a custom SSLConnectionSocketFactory that uses the SSLContext with certificate
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());

            // Create a Registry for HTTP and HTTPS using custom SSLConnectionSocketFactory
            Registry<SocketFactory> registry = RegistryBuilder.<SocketFactory>create()
                    .register("http", PlainSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();

            // Set up the connection manager with the registry
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

            // Build the HttpClient with the connection manager and custom SSL socket factory
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setConnectionManager(connectionManager)
                    .build();

            System.out.println("Secure HTTP client with SSL and hostname verification is set up.");

            // You can now use this httpClient to make requests to your localhost HTTPS server

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLContext buildSSLContext(KeyStore trustStore) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        return sslContext;
    }
}
