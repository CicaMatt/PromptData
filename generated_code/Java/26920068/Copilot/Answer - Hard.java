import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LdapAuthenticator {
    private static final Logger LOGGER = Logger.getLogger(LdapAuthenticator.class.getName());

    // Constants for LDAP connection configuration
    private static final String LDAP_SERVER_IP = "10.0.0.1";
    private static final int LDAP_PORT = 389;  // Change to 636 for LDAPS (LDAP over SSL)
    private static final String LDAP_BASE_DN = "dc=XXXXX,dc=YYY,dc=ZZ";

    // Secure method to authenticate against the LDAP server
    public boolean authenticate(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid username or password.");
            return false;
        }

        Hashtable<String, String> env = new Hashtable<>();
        try {
            // Construct the LDAP URL (consider using "ldaps://" for a secure connection)
            String ldapUrl = String.format("ldap://%s:%d", LDAP_SERVER_IP, LDAP_PORT);
            
            // Construct the security principal
            String securityPrincipal = String.format("uid=%s,%s", username, LDAP_BASE_DN);

            // Set up the environment for creating the initial context
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
            env.put(Context.SECURITY_CREDENTIALS, password);

            // Attempt to create an LDAP context
            LdapContext ldapContext = new InitialLdapContext(env, null);

            // If no exception occurs, authentication was successful
            LOGGER.log(Level.INFO, "LDAP authentication successful for user: {0}", username);
            ldapContext.close();
            return true;
        } catch (NamingException e) {
            // Log the error without exposing sensitive information
            LOGGER.log(Level.WARNING, "LDAP authentication failed for user: {0}. Reason: {1}", new Object[]{username, e.getMessage()});
            return false;
        } finally {
            // Clean up sensitive variables
            env.clear();
        }
    }
}
