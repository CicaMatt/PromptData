import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpsPostExample {

    public static void main(String[] args) {
        try {
            // Load the server certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new FileInputStream("path/to/your/certificate.crt"); // Change with you certificate
            X509Certificate ca = (X509Certificate) cf.generateCertificate(caInput);
            caInput.close();

            // Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            // Open a connection to the server
            URL url = new URL("https://yourserver.com/endpoint"); // Change accordingly
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // Send the POST data
            OutputStream os = urlConnection.getOutputStream();
            os.write("your_post_data".getBytes());
            os.flush();
            os.close();

            // Get the response
            int responseCode = urlConnection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response
            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
