import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapAuthenticator {

    private LdapContext ldap;

    public boolean authenticate(String user, String password) {
        String ldapServer = "10.0.0.1";
        int ldapPort = 389;
        String baseDN = "dc=XXXXX,dc=YYY,dc=ZZ"; // Replace with your actual base DN

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + ldapServer + ":" + ldapPort);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        // Construct the full DN using the provided 'user' and 'baseDN'
        String fullDN = "uid=" + user + "," + baseDN;
        env.put(Context.SECURITY_PRINCIPAL, fullDN);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            ldap = new InitialLdapContext(env, null);
            System.out.println("LDAP authentication successful for user: " + user);
            return true;
        } catch (NamingException e) {
            System.err.println("LDAP authentication failed for user: " + user);
            e.printStackTrace(); // Log the exception for detailed analysis
            return false;
        }
    }

    // ... other methods in your class
}