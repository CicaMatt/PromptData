import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapAuthentication {

    private static final String LDAP_SERVER_IP = "10.0.0.1";
    private static final int LDAP_PORT = 389;
    private static final String LDAP_DOMAIN = "dc=XXXXX,dc=YYY,dc=ZZ"; // Replace with actual domain

    /**
     * Authenticates a user against the LDAP server.
     *
     * @param user     The LDAP username.
     * @param password The user's password.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String user, String password) {
        if (user == null || user.isEmpty() || password == null || password.isEmpty()) {
            System.err.println("User or password is null/empty.");
            return false;
        }

        String ldapUrl = "ldap://" + LDAP_SERVER_IP + ":" + LDAP_PORT;
        String securityPrincipal = "uid=" + user + "," + LDAP_DOMAIN;

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        System.out.println("Connecting to: " + ldapUrl);
        System.out.println("Security Principal: " + securityPrincipal);

        try (LdapContext ldapContext = new InitialLdapContext(env, null)) {
            System.out.println("LDAP Authentication successful for user: " + user);
            return true;
        } catch (NamingException e) {
            System.err.println("LDAP Authentication failed: " + e.getMessage());
            if (e.getMessage().contains("error code 49")) {
                System.err.println("Invalid credentials for user: " + user);
            }
            return false;
        }
    }

    public static void main(String[] args) {
        LdapAuthentication ldapAuth = new LdapAuthentication();
        String user = "tiagoadami"; // Example username
        String password = "your-password-here"; // Example password

        boolean isAuthenticated = ldapAuth.authenticate(user, password);
        if (isAuthenticated) {
            System.out.println("User authenticated successfully.");
        } else {
            System.out.println("User authentication failed.");
        }
    }
}
