import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

public class LdapAuthentication {
    public static boolean authenticate(String user, String password) {
        try {
            // Set up the environment for creating the initial context
            Hashtable<String, Object> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://10.0.0.1:389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "uid=" + user + ",dc=XXXXX,dc=YYY,dc=ZZ");
            env.put(Context.SECURITY_CREDENTIALS, password);

            // Create the initial context
            DirContext ctx = new InitialDirContext(env);

            // Perform a search to check if the user exists and is valid
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = ctx.search("", "(uid=" + user + ")", controls);

            // If the user exists, check if their password is valid
            if (results != null && results.hasMore()) {
                SearchResult result = results.next();
                Attributes attributes = result.getAttributes();
                Attribute passwordAttribute = attributes.get("userPassword");

                // Compare the password hashes to ensure they match
                String passwordHash = (String) passwordAttribute.get();
                if (passwordHash != null && passwordHash.equals(password)) {
                    return true;
                }
            }
        } catch (NamingException e) {
            // Handle the exception
        }

        return false;
    }
}