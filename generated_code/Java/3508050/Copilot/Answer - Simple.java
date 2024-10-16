import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TrustedRootCertificates {
    public static void main(String[] args) throws Exception {
        // Load the default KeyStore
        String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
        FileInputStream is = new FileInputStream(cacertsPath);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "changeit".toCharArray()); // Default password for cacerts

        // Initialize TrustManagerFactory with the KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keystore);

        // Retrieve the list of trusted root certificates
        List<X509Certificate> trustedRootCertificates = Arrays.stream(tmf.getTrustManagers())
            .filter(X509TrustManager.class::isInstance)
            .map(X509TrustManager.class::cast)
            .flatMap(trustManager -> Arrays.stream(trustManager.getAcceptedIssuers()))
            .collect(Collectors.toList());

        // Print the trusted root certificates
        trustedRootCertificates.forEach(cert -> System.out.println(cert.getSubjectX500Principal()));
    }
}
