import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import java.util.Hashtable;

public class LdapAuthenticator {

    public boolean authenticate(String user, String password) {
        String ldapHost = "10.0.0.1";  // LDAP server IP
        int ldapPort = 389;  // Default LDAP port
        String baseDn = "dc=XXXXX,dc=YYY,dc=ZZ";  // Base DN

        String url = String.format("ldap://%s:%d", ldapHost, ldapPort);

        String securityPrincipal = String.format("uid=%s,%s", user, baseDn);

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            System.out.println("Connecting to: " + url);
            System.out.println("Using DN: " + securityPrincipal);

            // Attempt to create the context (bind)
            new InitialLdapContext(env, null);
            System.out.println("Authentication successful.");
            return true;
        } catch (NamingException e) {
            System.err.println("LDAP authentication failed.");
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        LdapAuthenticator ldapAuthenticator = new LdapAuthenticator();
        String username = "tiagoadami"; // Replace with actual username
        String password = "yourPassword"; // Replace with actual password

        boolean isAuthenticated = ldapAuthenticator.authenticate(username, password);
        System.out.println("Authenticated: " + isAuthenticated);
    }
}
