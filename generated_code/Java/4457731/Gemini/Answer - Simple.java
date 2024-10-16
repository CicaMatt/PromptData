import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MailSender {

    protected static Session initializeSession(MailMessage p_msg) throws Exception {
        Properties prop = System.getProperties();
        prop.put("mail.smtps.host", "test.mailserver.com");
        prop.put("mail.transport.protocol", "smtps");
        prop.put("mail.smtps.auth", true);

        // Potentially disable certificate validation for testing purposes only
        disableCertificateValidation(); 

        Session session = Session.getInstance(prop, null);
        session.setDebug(p_msg.getDebugMode());
        return session;
    }

    protected static void sendMessage(MimeMessage p_msg) throws Exception {
        Session session = Session.getDefaultInstance(System.getProperties(), null);
        Transport transport = session.getTransport("smtps");

        // Use the correct port for SMTPS (usually 465)
        transport.connect("test.mailserver.com", 465, "test.user@test.com", "testpwd"); 

        transport.sendMessage(p_msg, p_msg.getAllRecipients());
        transport.close();
    }

    // WARNING: Disabling certificate validation is insecure and should only be used for testing
    private static void disableCertificateValidation() throws Exception {
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

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}