import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class LdapAuthenticator {

    public boolean authenticate(String user, String password) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        // Replace with your actual LDAP server IP and port
        env.put(Context.PROVIDER_URL, "ldap://10.0.0.1:389"); 

        // Replace with your actual base DN
        String securityPrincipal = "uid=" + user + ",dc=XXXXX,dc=YYY,dc=ZZ"; 
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // Enhanced error handling and debugging
        try {
            new InitialDirContext(env); 
            System.out.println("LDAP authentication successful for user: " + user);
            return true;
        } catch (NamingException e) {
            System.err.println("LDAP authentication failed for user: " + user);
            e.printStackTrace(); 

            // Check for specific error codes
            if (e.getMessage().contains("LDAP: error code 49")) {
                System.err.println("Invalid credentials provided.");
            } else if (e.getMessage().contains("LDAP: error code 32")) {
                System.err.println("User does not exist or is not found in the directory.");
            } else {
                System.err.println("An unexpected LDAP error occurred: " + e.getMessage());
            }
            return false;
        }
    }

    public static void main(String[] args) {
        LdapAuthenticator authenticator = new LdapAuthenticator();
        // Replace with your actual username and password
        if (authenticator.authenticate("tiagoadami", "your_password")) { 
            // Authentication successful, proceed with your application logic
        } else {
            // Authentication failed, handle the error appropriately
        }
    }
}