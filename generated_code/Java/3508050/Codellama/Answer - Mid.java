import java.security.KeyStore;
import java.security.Security;

public class TrustedRootsExample {
    public static void main(String[] args) throws Exception {
        // Get a reference to the default KeyStore instance
        KeyStore keyStore = Security.getKeyStore();

        // Get all trusted root certificates in the JRE
        List<TrustAnchor> trustAnchors = (List<TrustAnchor>)keyStore.getTrustedCertificates();

        // Print each trust anchor
        for (TrustAnchor trustAnchor : trustAnchors) {
            System.out.println(trustAnchor);
        }
    }
}