import java.net.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.InputStream;

public class HTTPSClient {

    public static void main(String[] args) {
        try {
            // Trust manager that optionally bypasses server certificate validation for development/testing
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            };

            // Create SSL context, use "TLS" for secure communication
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            // Set default hostname verifier (if you need to skip hostname verification in development)
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    // Accept any hostname for development; change for production
                    return true; 
                }
            });

            // Create HTTPS connection
            URL url = new URL("https://example.com");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Set the SSL context on the connection
            conn.setSSLSocketFactory(sc.getSocketFactory());

            // Set connection properties
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Write the POST data
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write("param1=value1&param2=value2");
                writer.flush();
            }

            // Read the response
            try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                int data;
                StringBuilder response = new StringBuilder();
                while ((data = reader.read()) != -1) {
                    response.append((char) data);
                }
                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
