import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsPostVariant {

    public static void main(String[] args) {
        try {
            // Configura un trust manager che accetta tutti i certificati
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClient(X509Certificate[] certs, String authType) throws CertificateException {
                    }
                    public void checkServer(X509Certificate[] certs, String authType) throws CertificateException {
                    }
                }
            };

            // Inizializza il contesto SSL con il trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Configura un hostname verifier che accetta tutti gli host
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            // URL e payload
            URL url = new URL("https://yourserver.com/endpoint");
            String payload = "{\"key\":\"value\"}";

            // Apri la connessione e imposta le proprietà
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Scrivi il payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharset.UTF_8);
                os.write(input, 0, input.length);
            }

            // Ottieni la risposta
            int responseCode = connection.getResponseCode();
            System.out.println("Codice di risposta: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
