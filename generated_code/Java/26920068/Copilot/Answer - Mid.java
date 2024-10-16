import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapAuthentication {

    // Method to authenticate user with LDAP
    public boolean authenticate(String user, String password) {
        // Ensure the password is not null or empty
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            System.err.println("User or password is null/empty.");
            return false;
        }

        String ldapHost = "10.0.0.1"; // LDAP server IP
        int ldapPort = 389;           // Default LDAP port (389)
        String baseDN = "dc=XXXXX,dc=YYY,dc=ZZ"; // Replace with your DN

        // Construct the LDAP URL
        String ldapUrl = String.format("ldap://%s:%d", ldapHost, ldapPort);

        // Construct the security principal (user DN)
        String securityPrincipal = String.format("uid=%s,%s", user, baseDN);

        // Set up the LDAP environment properties
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password); // Plain-text password

        try {
            // Initialize the LDAP context for authentication
            LdapContext ldapContext = new InitialLdapContext(env, null);
            System.out.println("Authentication successful.");
            ldapContext.close(); // Close the context after use
            return true;
        } catch (NamingException e) {
            // Handle different LDAP error codes appropriately
            if (e.getMessage().contains("LDAP: error code 49")) {
                System.err.println("Invalid credentials (LDAP Error 49).");
            } else {
                System.err.println("LDAP Authentication failed: " + e.getMessage());
            }
            return false;
        }
    }

    public static void main(String[] args) {
        LdapAuthentication ldapAuth = new LdapAuthentication();
        
        // Test LDAP authentication with username and password
        boolean isAuthenticated = ldapAuth.authenticate("tiagoadami", "your_password_here");
        
        if (isAuthenticated) {
            System.out.println("User successfully authenticated.");
        } else {
            System.err.println("Authentication failed.");
        }
    }
}
