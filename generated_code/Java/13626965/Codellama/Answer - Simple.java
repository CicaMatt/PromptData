import java.net.*;
import java.security.cert.*;

public class IgnoreSSLException {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.abc.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Disable SSL certificate verification
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());

        // Rest of your code here...
    }
}