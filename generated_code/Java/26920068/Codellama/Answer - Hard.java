import java.util.HashMap;
import javax.naming.*;

public class Authenticate {
    public static void main(String[] args) throws NamingException {
        // LDAP server URL, username and password
        String url = "ldap://10.0.0.1:389"; 
        String dn = "uid=tiagoadami,dc=XXXXX,dc=YYY,dc=ZZ"; 
        String password = "password";

        HashMap<String, String> env = new HashMap<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, dn);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            InitialLdapContext ctx = new InitialLdapContext(env, null);
            System.out.println("Authenticated successfully");
        } catch (AuthenticationException e) {
            System.err.println("Error authenticating: " + e.getMessage());
        }
    }
}