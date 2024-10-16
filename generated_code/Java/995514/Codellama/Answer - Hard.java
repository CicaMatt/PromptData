import java.net.*;
import javax.net.ssl.*;

public class HTTPSClient {
    public static void main(String[] args) throws Exception {
        // Create a trust manager that trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        } };

        // Create a SSL context that uses the trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);

        // Create an HTTPS URL connection
        URL url = new URL("https://example.com");
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

        // Set the SSL context on the connection
        conn.setSSLSocketFactory(sc.getSocketFactory());

        // Send the POST request to the server
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write("Hello, world!");
        writer.flush();

        // Get the response from the server
        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        String response = "";
        while ((response += reader.readLine()) != null) {}

        System.out.println(response);
    }
}