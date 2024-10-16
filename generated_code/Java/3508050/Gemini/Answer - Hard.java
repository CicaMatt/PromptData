import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class TrustedRootCertificates {

    public static void main(String[] args) throws Exception {

        // Access the default "cacerts" keystore
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null); 

        // Iterate over the keystore entries (aliases)
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();

            // Retrieve the certificate associated with the alias
            Certificate cert = keyStore.getCertificate(alias);
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;

                // Check if the certificate is a trusted root certificate
                if (x509Cert.getSubjectX500Principal().equals(x509Cert.getIssuerX500Principal())) {
                    System.out.println("Trusted Root Certificate:");
                    System.out.println("Alias: " + alias);
                    System.out.println("Subject: " + x509Cert.getSubjectX500Principal());
                    System.out.println("------------------------");
                }
            }
        }
    }
}