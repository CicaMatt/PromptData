import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrustedRootCertificates {

    public static void main(String[] args) throws Exception {
        // Initialize the TrustManagerFactory with the default algorithm
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        // Retrieve the list of trusted certificates
        List<X509Certificate> trustedCertificates = Arrays.stream(trustManagerFactory.getTrustManagers())
                .filter(X509TrustManager.class::isInstance)
                .map(X509TrustManager.class::cast)
                .flatMap(trustManager -> Arrays.stream(trustManager.getAcceptedIssuers()))
                .collect(Collectors.toList());

        // Print out the trusted certificates
        trustedCertificates.forEach(cert -> System.out.println(cert.getSubjectX500Principal()));
    }
}
