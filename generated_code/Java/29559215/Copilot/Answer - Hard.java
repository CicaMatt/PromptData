import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.PublicKey;

public class PublicKeyFetcher {

    // Method to fetch public keys from a secured HTTPS website
    public static void getPublicKeys(String httpsUrl) {
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(SSLContextBuilder.create()
                        .loadTrustMaterial(new TrustStrategy() {
                            @Override
                            public boolean isTrusted(X509Certificate[] chain, String authType) {
                                return true;  
                            }
                        })
                        .build())
                .build()) {

            HttpGet request = new HttpGet(httpsUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {

                Certificate[] certs = response.getEntity().getContent().getClass().getCertificates();

                // Iterate over the certificates and extract public keys
                for (Certificate cert : certs) {
                    if (cert instanceof X509Certificate) {
                        X509Certificate x509Cert = (X509Certificate) cert;
                        PublicKey publicKey = x509Cert.getPublicKey();
                        System.out.println("Public Key: " + publicKey);
                    }
                }

            }
        } catch (SSLPeerUnverifiedException e) {
            System.err.println("Failed to verify peer: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage - replace with the URL of the secured website
        String httpsUrl = "https://www.example.com";
        getPublicKeys(httpsUrl);
    }
}
