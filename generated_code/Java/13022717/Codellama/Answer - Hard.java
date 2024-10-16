import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class TestSSL {
    public static void main(String[] args) throws Exception {
        // Carica il trust store predefinito del sistema
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null); // Carica il keystore predefinito
        tmf.init(ks);

        // Inizializza il contesto SSL con i trust manager di default
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Creare una connessione HTTPS sicura
        URL url = new URL("https://www.google.com");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        
        // Verifica che il certificato del server sia valido
        con.setSSLSocketFactory(sc.getSocketFactory());

        // Leggi la risposta dalla connessione
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
