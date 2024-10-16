import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.PublicKey;

public class SSLPublicKeyFetcher {

    public static void main(String[] args) {
        String httpsUrl = "https://www.example.com"; // Replace with the website URL you want to get the public key from

        try {
            // Establish HTTPS connection
            URL url = new URL(httpsUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // Retrieve the server certificates
            Certificate[] certs = connection.getServerCertificates();

            // Loop through certificates and extract public keys
            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    PublicKey publicKey = x509Cert.getPublicKey();

                    // Print public key details
                    System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
                    System.out.println("Public Key Format: " + publicKey.getFormat());
                    System.out.println("Public Key: " + publicKey);
                }
            }
        } catch (SSLPeerUnverifiedException e) {
            System.out.println("SSLPeerUnverifiedException: Could not verify the SSL certificate.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: Error during the connection.");
            e.printStackTrace();
        }
    }
}
