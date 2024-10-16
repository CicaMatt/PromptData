import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.PublicKey;

public class PublicKeyFetcher {

    public static void main(String[] args) {
        try {
            // Replace with the website you want to connect to
            String httpsUrl = "https://www.example.com";
            URL url = new URL(httpsUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();

            // Retrieve the server certificates
            Certificate[] certs = conn.getServerCertificates();

            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    PublicKey publicKey = x509Cert.getPublicKey();
                    System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
                    System.out.println("Public Key Format: " + publicKey.getFormat());
                    System.out.println("Public Key: " + publicKey);
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
