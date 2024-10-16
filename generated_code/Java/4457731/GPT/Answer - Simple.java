import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class MailSender {

    public static void main(String[] args) {
        try {
            // Create a sample MimeMessage
            MimeMessage message = createMessage();

            // Initialize session and send message
            Session session = initializeSession();
            sendMessage(session, message);
            
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a simple MimeMessage
    private static MimeMessage createMessage() throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("test.user@test.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("recipient@test.com"));
        message.setSubject("Test Email");
        message.setText("This is a test email.");

        return message;
    }

    // Initialize a mail session with properties
    protected static Session initializeSession() throws Exception {
        // Disable SSL certificate validation (NOT for production use)
        disableSSLValidation();

        Properties prop = new Properties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", "true");
        prop.put("mail.smtps.port", "465");
        prop.put("mail.smtps.ssl.enable", "true");
        prop.put("mail.smtps.ssl.trust", "*"); // Trust all SSL certificates (temporary for testing)

        return Session.getInstance(prop);
    }

    // Send the email message
    protected static void sendMessage(Session session, MimeMessage message) throws Exception {
        Transport transport = session.getTransport("smtps");
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    // Disable SSL certificate validation (for testing only)
    private static void disableSSLValidation() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLContext.setDefault(sc);
    }
}
