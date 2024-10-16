import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class LdapAuthentication {

    public boolean authenticate(String user, String password) {
        String ldapUrl = "ldap://10.0.0.1:389"; // Replace with your LDAP server IP and port
        
        // Full distinguished name (DN) for the user
        String securityPrincipal = "uid=" + user + ",dc=XXXXX,dc=YYY,dc=ZZ";

        // Environment settings for LDAP
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            // Attempt to connect and authenticate with the provided credentials
            LdapContext ctx = new InitialLdapContext(env, null);
            System.out.println("Authentication successful for user: " + user);
            ctx.close();
            return true;
        } catch (NamingException e) {
            // Authentication failure or connection issues
            System.err.println("LDAP Authentication failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        LdapAuthentication ldapAuth = new LdapAuthentication();
        
        // Replace these with the actual user and password
        String user = "tiagoadami";
        String password = "your_password_here";
        
        boolean isAuthenticated = ldapAuth.authenticate(user, password);
        if (isAuthenticated) {
            System.out.println("User " + user + " successfully authenticated.");
        } else {
            System.out.println("Authentication failed for user " + user + ".");
        }
    }
}
