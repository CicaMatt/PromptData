import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import java.util.Properties;

public class MailSender {
    public static void main(String[] args) {
        try {
            String smtpHost = args[0];
            String username = args[1];
            String password = args[2];
            int port = Integer.parseInt(args[3]);

            Properties props = new Properties();
            props.put("mail.smtps.host", smtpHost);
            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.auth", true);

            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtps");

            transport.connect(smtpHost, port, username, password);

            MimeMessage msg = new MimeMessage(session);
            msg.setSubject("Test email");
            msg.setText("This is a test email.");

            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
