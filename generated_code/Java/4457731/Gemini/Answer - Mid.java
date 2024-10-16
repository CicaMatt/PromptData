import java.security.Security;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class SecureEmailSender {

    protected static Session initializeSession(MailMessage p_msg) throws Exception {
        Properties prop = System.getProperties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", true);
        prop.put("mail.smtps.port", 465); 

        // If necessary, configure SSL context to trust all certificates (use with caution in production)
        if (p_msg.getTrustAllCertificates()) {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
            SSLContext.setDefault(sslContext);
            prop.put("mail.smtps.ssl.trust", "*");
        }

        Session session = Session.getInstance(prop, null);
        session.setDebug(p_msg.getDebugMode());
        return session;
    }

    protected static void sendMessage(MimeMessage p_msg) throws Exception {
        Session session = initializeSession(new MailMessage(true, true)); 
        Transport transport = session.getTransport("smtps");

        // Connect using explicit port
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd"); 

        transport.sendMessage(p_msg, p_msg.getAllRecipients());
        transport.close();
    }

    // Helper class to encapsulate mail message properties
    static class MailMessage {
        private boolean debugMode;
        private boolean trustAllCertificates;

        public MailMessage(boolean debugMode, boolean trustAllCertificates) {
            this.debugMode = debugMode;
            this.trustAllCertificates = trustAllCertificates;
        }

        // ... other methods (getDebugMode, getTrustAllCertificates, etc.)
    }

    // TrustManager implementation to trust all certificates (use with caution in production)
    static class TrustAllX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
}