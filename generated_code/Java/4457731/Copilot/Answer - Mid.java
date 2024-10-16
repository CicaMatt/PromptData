import javax.mail.*;
import javax.mail.internet.*;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.util.Properties;

public class MailSender {

    public static void main(String[] args) {
        try {
            MimeMessage message = new MimeMessage(initializeSession());
            message.setFrom(new InternetAddress("test.user@test.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("recipient@test.com"));
            message.setSubject("Test Mail");
            message.setText("This is a test mail.");

            sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Session initializeSession() throws Exception {
        Properties prop = System.getProperties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", true);

        // Load custom truststore
        FileInputStream trustStoreStream = new FileInputStream("path/to/truststore.jks");
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(trustStoreStream, "truststore-password".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        prop.put("mail.smtps.ssl.socketFactory", sslSocketFactory);

        Session session = Session.getInstance(prop, null);
        session.setDebug(true);
        return session;
    }

    protected static void sendMessage(MimeMessage p_msg) throws Exception {
        Properties prop = System.getProperties();
        Session session = Session.getDefaultInstance(prop, null);
        Transport transport = session.getTransport("smtps");
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd");
        transport.sendMessage(p_msg, p_msg.getAllRecipients());
        transport.close();
    }
}
