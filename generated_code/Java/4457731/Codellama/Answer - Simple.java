import java.util.Properties;
import javax.mail.*;
import javax.net.ssl.SSLSocketFactory;

public class MailSender {
    private static final String HOST = "test.mailserver.com";
    private static final int PORT = 465;
    private static final String USERNAME = "test.user@test.com";
    private static final String PASSWORD = "testpwd";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("mail.smtps.host", HOST);
        props.put("mail.smtps.port", PORT);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom("test.user@test.com");
            message.addRecipient(Message.RecipientType.TO, "recipient@example.com");
            message.setText("This is a test email.");

            Transport transport = session.getTransport("smtps");
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}