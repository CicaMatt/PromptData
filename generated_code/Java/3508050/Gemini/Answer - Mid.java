import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class TrustedRootCertificates {

    public static void main(String[] args) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null); // Load default cacerts keystore

        for (String alias : keyStore.aliases()) {
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) certificate;
                System.out.println("Trusted Root Certificate:");
                System.out.println("  Subject DN: " + x509Certificate.getSubjectDN());
                System.out.println("  Issuer DN: " + x509Certificate.getIssuerDN());
                // Access other certificate details as needed
            }
        }
    }
}