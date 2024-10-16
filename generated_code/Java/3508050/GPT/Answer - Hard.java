import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Optional;
import java.nio.file.Path;
import javax.security.auth.x500.X500Principal;

public class TrustedRootCertificates {

    public static void main(String[] args) {
        try {
            // Path to the JRE's cacerts file (default in JAVA_HOME/lib/security/cacerts)
            Path cacertsPath = Paths.get(System.getProperty("java.home"), "lib", "security", "cacerts");

            // Load the keystore password securely from an environment variable or external config
            char[] password = loadKeystorePassword()
                    .orElseThrow(() -> new RuntimeException("Keystore password is not set"));

            // Load the default JRE keystore using Files.newInputStream (modern API)
            try (var is = Files.newInputStream(cacertsPath, StandardOpenOption.READ)) {
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(is, password);

                // Enumerate all aliases (trusted root certificates)
                Enumeration<String> aliases = keystore.aliases();

                System.out.println("Trusted Root Certificates:");
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();

                    // Only process certificates (ignore other key types)
                    if (keystore.isCertificateEntry(alias)) {
                        X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);

                        // Use the non-deprecated methods to retrieve subject and issuer
                        X500Principal subjectPrincipal = cert.getSubjectX500Principal();
                        X500Principal issuerPrincipal = cert.getIssuerX500Principal();

                        // Print certificate information
                        System.out.println("Alias: " + alias);
                        System.out.println("Subject: " + subjectPrincipal.getName());
                        System.out.println("Issuer: " + issuerPrincipal.getName());
                        System.out.println("Valid From: " + cert.getNotBefore());
                        System.out.println("Valid To: " + cert.getNotAfter());
                        System.out.println("------------------------------------------------");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to securely load the keystore password from an environment variable or configuration file
    private static Optional<char[]> loadKeystorePassword() {
        // Retrieve the password from an environment variable, e.g., "KEYSTORE_PASSWORD"
        String passwordEnv = System.getenv("KEYSTORE_PASSWORD");
        if (passwordEnv != null && !passwordEnv.isEmpty()) {
            return Optional.of(passwordEnv.toCharArray());
        }

        // As a fallback, load from a file if required (for example, in secure environments)
        try {
            String passwordFilePath = System.getProperty("keystore.password.file");
            if (passwordFilePath != null) {
                String password = Files.readString(Paths.get(passwordFilePath)).trim();
                if (!password.isEmpty()) {
                    return Optional.of(password.toCharArray());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load keystore password from file: " + e.getMessage());
        }

        // Return empty if no password found
        return Optional.empty();  
    }
}
