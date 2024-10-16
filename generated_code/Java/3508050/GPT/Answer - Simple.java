import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.io.FileInputStream;

public class TrustedRootCertificates {
    public static void main(String[] args) {
        try {
            // Load the default JRE keystore (cacerts)
            String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
            FileInputStream is = new FileInputStream(cacertsPath);
            
            // Load the JRE's default keystore
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = "changeit".toCharArray(); // Default password for cacerts
            keystore.load(is, password);

            // Iterate through all the entries in the keystore
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (keystore.isCertificateEntry(alias)) {
                    X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);
                    System.out.println("Alias: " + alias);
                    System.out.println(cert);
                    System.out.println("-----------------------------------");
                }
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
