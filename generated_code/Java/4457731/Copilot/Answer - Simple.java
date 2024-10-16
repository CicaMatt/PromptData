import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.net.ssl.*;

public class MailSender {

    public static void main(String[] args) {
        try {
            MailMessage msg = new MailMessage();
            msg.setDebugMode(true);
            msg.setRecipients("recipient@example.com");
            msg.setSubject("Test Mail");
            msg.setBody("This is a test mail.");

            Session session = initializeSession(msg);
            MimeMessage message = createMessage(session, msg);
            sendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Session initializeSession(MailMessage p_msg) throws GeneralSecurityException {
        Properties prop = System.getProperties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", "true");

        // Trust all certificates for testing purposes
        SSLContext ctx = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
        ctx.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory sf = ctx.getSocketFactory();
        prop.put("mail.smtps.ssl.socketFactory", sf);

        Session session = Session.getInstance(prop, null);
        session.setDebug(p_msg.getDebugMode());
        return session;
    }

    protected static MimeMessage createMessage(Session session, MailMessage p_msg) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("test.user@test.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(p_msg.getRecipients()));
        message.setSubject(p_msg.getSubject());
        message.setText(p_msg.getBody());
        return message;
    }

    protected static void sendMessage(Session session, MimeMessage p_msg) throws MessagingException {
        Transport transport = session.getTransport("smtps");
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd");
        transport.sendMessage(p_msg, p_msg.getAllRecipients());
        transport.close();
    }
}

class MailMessage {
    private boolean debugMode;
    private String recipients;
    private String subject;
    private String body;

    public boolean getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
