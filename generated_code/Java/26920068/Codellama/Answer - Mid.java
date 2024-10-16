import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class LdapAuthentication {
    public static boolean authenticate(String user, String password) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.0.0.1:389");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, String.format("uid=%s,dc=XXXXX,dc=YYY,dc=ZZ", user));
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext context = new InitialDirContext(env);
        Attributes attributes = context.getAttributes("uid=tiagoadami,dc=XXXXX,dc=YYY,dc=ZZ", new String[] { "+" });
        Attribute attribute = attributes.get("+");
        NamingEnumeration<?> enumeration = attribute.getAll();
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }

        context.close();

        return true;
    }
}