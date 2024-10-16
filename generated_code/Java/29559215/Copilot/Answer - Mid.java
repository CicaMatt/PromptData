import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class PublicKeyExtractor {

    public static void main(String[] args) {
        String host = "your-secure-website.com"; // Replace with the desired secured website
        int port = 443; 

        try {
            // Create an SSL socket to connect to the host
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.startHandshake();

            // Retrieve the certificates from the SSL connection
            Certificate[] certs = socket.getSession().getPeerCertificates();

            for (Certificate cert : certs) {
                if (cert instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) cert;
                    // Get the public key from the certificate
                    System.out.println("Public Key: " + x509Cert.getPublicKey());
                }
            }

            socket.close();
        } catch (SSLPeerUnverifiedException e) {
            System.err.println("Could not verify peer: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
        }
    }
}
