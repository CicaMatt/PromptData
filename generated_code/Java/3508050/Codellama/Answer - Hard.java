import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class TrustedRootsExample {
  public static void main(String[] args) throws Exception {
    // Get the default system keystore
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    
    // Get an enumeration of all trusted root certificates in the keystore
    Enumeration<String> aliases = keyStore.aliases();
    while (aliases.hasMoreElements()) {
      String alias = aliases.nextElement();
      Certificate certificate = keyStore.getCertificate(alias);
      
      // Check if the certificate is a trusted root
      if (certificate != null && certificate.isTrustedRoot()) {
        System.out.println("Found trusted root: " + alias);
      }
    }
  }
}