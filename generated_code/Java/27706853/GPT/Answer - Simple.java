import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;

public class ODKSSLBypass {

    public static void main(String[] args) {
        try {
            // Disable SSL certificate validation
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

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create a hostname verifier that ignores the host name mismatch
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Set the default hostname verifier to ignore host verification
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            // Example HTTP request to localhost
            URL url = new URL("https://localhost:8443"); // Adjust port as needed
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print response
            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
