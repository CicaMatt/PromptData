import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.util.Scanner;

public class TrustedRootCertificates {

    public static void main(String[] args) throws Exception {
        // Load the default Java truststore
        String trustStorePath = System.getProperty("java.home") + "/lib/security/cacerts";
        char[] trustStorePassword = getTrustStorePassword();

        FileInputStream trustStoreStream = new FileInputStream(trustStorePath);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(trustStoreStream, trustStorePassword);

        // Initialize TrustManagerFactory with the truststore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        // Retrieve the list of trusted root certificates
        List<X509Certificate> trustedRootCertificates = Arrays.stream(trustManagerFactory.getTrustManagers())
            .filter(X509TrustManager.class::isInstance)
            .map(X509TrustManager.class::cast)
            .flatMap(trustManager -> Arrays.stream(trustManager.getAcceptedIssuers()))
            .collect(Collectors.toList());

        // Print the trusted root certificates
        trustedRootCertificates.forEach(cert -> System.out.println(cert.getSubjectX500Principal()));

        // Clear the password from memory
        Arrays.fill(trustStorePassword, ' ');
    }

    private static char[] getTrustStorePassword() {
        // Securely read the truststore password from the console
        System.out.print("Enter truststore password: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toCharArray();
    }
}
