import javax.mail.*;
import javax.mail.internet.*;
import javax.net.ssl.SSLSocketFactory;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureMailService {

    private static final Logger LOGGER = Logger.getLogger(SecureMailService.class.getName());

    // Load mail server configuration from environment variables
    private static final String MAIL_HOST = System.getenv("MAIL_SERVER_HOST");
    private static final String MAIL_PORT = System.getenv("MAIL_SERVER_PORT");
    private static final String MAIL_USER = System.getenv("MAIL_SERVER_USER");
    private static final String MAIL_PASSWORD = System.getenv("MAIL_SERVER_PASSWORD");

    // Initialize session with proper TLS settings
    protected static Session initializeSession(boolean debugMode) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", MAIL_HOST);
        props.put("mail.smtp.port", MAIL_PORT);  // Standard port for STARTTLS is 587
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  // Enabling STARTTLS

        // SSL Factory settings
        props.put("mail.smtp.socketFactory.class", SSLSocketFactory.class.getName());
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.trust", MAIL_HOST);  // Trust the server certificate (use cautiously in production)

        // Create an authenticator to securely handle credentials
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USER, MAIL_PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);
        session.setDebug(debugMode);

        return session;
    }

    // Send the email message securely
    protected static void sendMessage(MimeMessage message) throws MessagingException {
        try {
            Session session = initializeSession(message.getDebug());
            Transport transport = session.getTransport("smtp");
            try {
                transport.connect(MAIL_HOST, Integer.parseInt(MAIL_PORT), MAIL_USER, MAIL_PASSWORD);
                transport.sendMessage(message, message.getAllRecipients());
            } finally {
                transport.close();
            }
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email: {0}", e.getMessage());
            throw e;  // rethrow the exception after logging
        }
    }

    // Build MimeMessage (example method to build an email)
    protected static MimeMessage createMessage(Session session, String to, String from, String subject, String body)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}
