import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;

public class SecureWebsitePublicKeyRetrieval {

    public static void main(String[] args) {
        String websiteUrl = "https://www.example.com"; // Replace with your target URL

        try {
            // Create an HTTPS connection to the website
            URL url = new URL(websiteUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Retrieve the server's certificate chain
            Certificate[] serverCertificates = connection.getServerCertificates();

            // Extract the public key from the first certificate (usually the server's own certificate)
            if (serverCertificates.length > 0) {
                X509Certificate cert = (X509Certificate) serverCertificates[0];
                byte[] publicKeyBytes = cert.getPublicKey().getEncoded();

                // Print or process the public key bytes as needed (e.g., Base64 encode, store in a file, etc.)
                System.out.println("Public Key (bytes): " + publicKeyBytes); 
            } else {
                System.out.println("No server certificates found.");
            }

            connection.disconnect();

        } catch (IOException | CertificateEncodingException e) {
            e.printStackTrace();
        }
    }
}