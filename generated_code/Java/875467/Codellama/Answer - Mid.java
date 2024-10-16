import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.ValidatorException;
import javax.net.ssl.PKIXPathBuildingFailed;

public class ClientCertificateExample {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://somehost.dk:3049");
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        // Try using the default SSLSocketFactory
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        conn.setSSLSocketFactory(sslsocketfactory);

        InputStream inputstream;
        try {
            inputstream = conn.getInputStream();
        } catch (SSLHandshakeException e) {
            // The error message suggests that the client certificate is not correct or valid
            System.out.println("Received fatal alert: handshake_failure");
        } catch (ValidatorException e) {
            // The error message suggests that there is a problem with the server root certificate
            System.out.println("PKIX path building failed");
        } catch (PKIXPathBuildingFailed e) {
            // The error message suggests that there is a problem with the client certificate
            System.out.println("Received fatal alert: handshake_failure");
        }
    }
}