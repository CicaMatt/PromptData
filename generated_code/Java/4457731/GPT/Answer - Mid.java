import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

public class MailService {

    private static final String SMTP_HOST = "test.mailserver.com";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_USER = "test.user@test.com";
    private static final String SMTP_PASSWORD = "testpwd";

    protected static Session initializeSession(boolean debugMode) throws Exception {
        Properties prop = new Properties();
        
        // Set mail server properties explicitly
        prop.put("mail.smtp.host", SMTP_HOST);
        prop.put("mail.smtp.port", SMTP_PORT);
        prop.put("mail.smtp.auth", "true");
        // Ensure SSL is enabled
        prop.put("mail.smtp.ssl.enable", "true"); 
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); 
        prop.put("mail.smtp.ssl.checkserveridentity", "true"); 

        // Create an authenticator for SMTP authentication
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        };

        // Create session
        Session session = Session.getInstance(prop, auth);
        // Set the debug mode based on input
        session.setDebug(debugMode); 
        
        return session;
    }

    protected static void sendMessage(MimeMessage message) throws Exception {
        // Initialize the session
        Session session = initializeSession(message.getDebug());

        // Transport setup and message sending
        Transport transport = session.getTransport("smtp");
        
        try {
            transport.connect(SMTP_HOST, SMTP_PORT, SMTP_USER, SMTP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close(); // Ensure the transport is closed properly
        }
    }
}
