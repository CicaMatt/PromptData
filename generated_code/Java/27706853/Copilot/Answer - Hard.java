import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

public class CustomSSLSocketFactory {

    public static SSLSocketFactory getSocketFactory() throws Exception {
        // Load your self-signed certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream caInput = new FileInputStream("path/to/your/certificate.crt");
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

    public static void main(String[] args) {
        try {
            SSLSocketFactory sslSocketFactory = getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            // Now you can make HTTPS requests using this custom SSLSocketFactory
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
