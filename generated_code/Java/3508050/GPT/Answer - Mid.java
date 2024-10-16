import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class TrustedRootCertificates {

    public static void main(String[] args) {
        try {
            // Path to the default Java truststore (cacerts)
            String javaHome = System.getProperty("java.home");
            String cacertsPath = javaHome + "/lib/security/cacerts";

            // Load the Java default keystore (cacerts)
            try (FileInputStream is = new FileInputStream(cacertsPath)) {
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

                // Default password for cacerts keystore
                char[] password = "changeit".toCharArray(); // Default password
                keystore.load(is, password);

                // Iterate over all aliases in the keystore
                Enumeration<String> aliases = keystore.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();

                    // Check if the entry is a trusted certificate
                    if (keystore.isCertificateEntry(alias)) {
                        Certificate cert = keystore.getCertificate(alias);
                        if (cert instanceof X509Certificate) {
                            X509Certificate x509Cert = (X509Certificate) cert;

                            // Print details of the trusted root certificate
                            System.out.println("Certificate Alias: " + alias);
                            System.out.println("Issuer: " + x509Cert.getIssuerX500Principal().getName());
                            System.out.println("Subject: " + x509Cert.getSubjectX500Principal().getName());
                            System.out.println("Valid From: " + x509Cert.getNotBefore());
                            System.out.println("Valid To: " + x509Cert.getNotAfter());
                            System.out.println("---------------------------------------------------");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
