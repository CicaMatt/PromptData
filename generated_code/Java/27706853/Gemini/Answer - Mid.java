import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class ODKSSLBypassWithPinning {

    public static void main(String[] args) throws Exception {

        // Load the certificate from a file (replace with your certificate path)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream fis = new FileInputStream("/path/to/your/localhost.cer"); 
        Certificate ca = cf.generateCertificate(fis);

        // Create a KeyStore containing our trusted certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the certificate in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Create all-trusting host name verifier (still needed for localhost)
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the SSLContext and HostnameVerifier
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // Now you can access an https URL 
        URL url = new URL("https://localhost:<your_port>/<your_endpoint>");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        // ... rest of your code to handle the connection
    }
}