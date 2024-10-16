import java.io.FileInputStream;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.ssl.TrustStoreLoader;

public class Mailer {
  private static final String SMTP_HOST = "test.mailserver.com";
  private static final int SMTP_PORT = 465;
  private static final String USERNAME = "test.user@test.com";
  private static final String PASSWORD = "testpwd";

  public static void main(String[] args) throws Exception {
    // Add truststore to JVM configuration
    Properties props = System.getProperties();
    FileInputStream trustStore = new FileInputStream("path/to/truststore");
    TrustStoreLoader loader = new TrustStoreLoader(props);
    loader.loadTrustStore(trustStore);

    // Create a session using the truststore configuration
    Session session = Session.getInstance(props, null);
    session.setDebug(true);

    // Create a message with recipient and sender details
    MimeMessage msg = new MimeMessage(session);
    msg.addRecipient("TO", "recipient@example.com");
    msg.addSender("FROM", "sender@example.com");
    msg.setSubject("Test email");
    msg.setText("This is a test email.");

    // Send the message using SMTP
    Transport transport = session.getTransport("smtps");
    transport.connect(SMTP_HOST, SMTP_PORT, USERNAME, PASSWORD);
    transport.sendMessage(msg, msg.getAllRecipients());
    transport.close();
  }
}