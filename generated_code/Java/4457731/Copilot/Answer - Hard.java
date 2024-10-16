import javax.mail.*;
import javax.mail.internet.*;
import java.security.KeyStore;
import javax.net.ssl.*;

public class MailSender {

    public static void main(String[] args) throws Exception {
        MailMessage p_msg = new MailMessage();
        p_msg.setDebugMode(true);
        Session session = initializeSession(p_msg);
        MimeMessage message = new MimeMessage(session);
        // Set your message details here
        sendMessage(message);
    }

    protected static Session initializeSession(MailMessage p_msg) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", true);

        Session session = Session.getInstance(prop, null);
        session.setDebug(p_msg.getDebugMode());
        return session;
    }

    protected static void sendMessage(MimeMessage p_msg) throws Exception {
        Properties prop = new Properties();
        Session session = Session.getDefaultInstance(prop, null);

        // Load the truststore
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream trustStoreStream = new FileInputStream("path/to/truststore")) {
            trustStore.load(trustStoreStream, "truststore-password".toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

        Transport transport = session.getTransport("smtps");
        ((SMTPTransport) transport).setSSLSocketFactory(sslContext.getSocketFactory());
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd");
        transport.sendMessage(p_msg, p_msg.getAllRecipients());
        transport.close();
    }
}

class MailMessage {
    private boolean debugMode;

    public boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
