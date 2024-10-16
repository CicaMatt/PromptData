import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import java.security.PublicKey;

public class GetPublicKey {

    public static void main(String[] args) {
        try {
            // URL of the secured website
            String httpsURL = "https://www.example.com";

            // Open connection to the URL
            URL url = new URL(httpsURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // Retrieve server certificates
            Certificate[] certs = connection.getServerCertificates();

            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;

                    // Get the public key from the certificate
                    PublicKey publicKey = x509Cert.getPublicKey();

                    // Print the public key
                    System.out.println("Public Key: " + publicKey.toString());
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
