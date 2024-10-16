import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class PublicKeyRetrieval {

    public static void main(String[] args) {
        String securedWebsiteURL = "https://www.example.com"; // Replace with your target URL

        try {
            URL url = new URL(securedWebsiteURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Retrieve the server's certificate chain
            Certificate[] serverCertificates = connection.getServerCertificates();

            // Extract the public key from the first certificate (usually the server's own certificate)
            X509Certificate x509Certificate = (X509Certificate) serverCertificates[0];
            byte[] encodedPublicKey = x509Certificate.getPublicKey().getEncoded();

            // Encode the public key (e.g., using Base64) for convenient representation
            String base64EncodedPublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);

            System.out.println("Public Key (Base64 encoded): " + base64EncodedPublicKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}