import javax.net.ssl.*;
import java.io.*;

public class HTTPSClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://example.com");
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        
        // Set the flag to accept all server certificates, even if they are not trusted
        connection.setSSLSocketFactory(new SSLSocketFactory() {
            @Override
            public Socket createSocket(String host, int port) throws IOException {
                return new Socket(host, port);
            }
            
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Always trust all server certificates
            }
        });
        
        // Make the POST request
        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write("POST / HTTP/1.1\r\n");
        writer.write("Host: example.com\r\n");
        writer.write("Content-Type: application/x-www-form-urlencoded\r\n");
        writer.write("\r\n");
        writer.flush();
        
        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}