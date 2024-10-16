import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class SSLPublicKeyRetriever {

    public static void main(String[] args) {
        try {
            // Define the URL of the secured website
            String httpsUrl = "https://example.com";  // Replace with the target URL
            URL url = new URL(httpsUrl);

            // Open a connection to the URL
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // Retrieve the certificates from the connection
            Certificate[] certs = connection.getServerCertificates();

            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    X509Certificate x509cert = (X509Certificate) cert;

                    // Extract the public key from the certificate
                    PublicKey publicKey = x509cert.getPublicKey();
                    System.out.println("Public Key: " + publicKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
