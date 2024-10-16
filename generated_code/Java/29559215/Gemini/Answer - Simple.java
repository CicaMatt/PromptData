import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class RetrievePublicKeys {

    public static void main(String[] args) {
        String securedWebsiteURL = "https://www.example.com"; // Replace with your target URL

        try {
            URL url = new URL(securedWebsiteURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            SSLSession sslSession = connection.getSSLSession();
            Certificate[] serverCertificates = sslSession.getPeerCertificates();

            for (Certificate certificate : serverCertificates) {
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) certificate;
                    System.out.println("Public Key:");
                    System.out.println(Arrays.toString(x509Certificate.getPublicKey().getEncoded()));
                    // You can further process or store the public key as needed
                }
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}