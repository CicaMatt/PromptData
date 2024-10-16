import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import java.util.Hashtable;
import java.nio.charset.StandardCharsets;

public class LdapAuthenticator {

    private static final String LDAP_SERVER_IP = "10.0.0.1";  // Externalize this as configuration
    private static final int LDAP_SERVER_PORT = 389;  // Externalize this as configuration
    private static final String LDAP_DN_TEMPLATE = "uid=%s,dc=XXXXX,dc=YYY,dc=ZZ";  // Externalize this as configuration

    public boolean authenticate(String user, String password) {
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            // Log a warning and return false if credentials are empty
            return false;
        }

        // Construct LDAP URL
        String ldapUrl = String.format("ldap://%s:%d", LDAP_SERVER_IP, LDAP_SERVER_PORT);

        // Construct the security principal (DN)
        String securityPrincipal = String.format(LDAP_DN_TEMPLATE, user);

        // Prepare environment for LDAP connection
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, new String(password.getBytes(StandardCharsets.UTF_8)));

        InitialLdapContext ldapContext = null;
        try {
            ldapContext = new InitialLdapContext(env, null);
            // Authentication successful
            return true;
        } catch (NamingException e) {
            // Handle the exception, logging in a secure way without exposing sensitive information
            // You can log or handle specific error codes here if needed
            System.err.println("LDAP Authentication failed. Error: " + e.getMessage());
            return false;
        } finally {
            // Properly close the context if it was opened
            if (ldapContext != null) {
                try {
                    ldapContext.close();
                } catch (NamingException e) {
                    // Handle the exception during context closure
                    System.err.println("Error closing LDAP context: " + e.getMessage());
                }
            }
        }
    }
}
