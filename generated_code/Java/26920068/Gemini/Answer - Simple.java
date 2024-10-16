import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class LdapAuthenticator {

    public boolean authenticate(String user, String password) {
        String ldapUrl = "ldap://10.0.0.1:389"; // LDAP server URL
        String baseDn = "dc=XXXXX,dc=YYY,dc=ZZ"; // Your LDAP base DN
        String userDn = "uid=" + user + "," + baseDn; // Full user DN

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            // Use InitialDirContext for LDAP authentication
            new InitialDirContext(env); 
            return true; // Authentication successful
        } catch (NamingException e) {
            System.err.println("LDAP authentication failed: " + e.getMessage());
            // Optionally, log the stack trace for debugging: e.printStackTrace();
            return false;
        }
    }
}