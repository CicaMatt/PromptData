import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SecureApiRequest {

    public static void main(String[] args) {

        try {
            // Load your app's truststore (if needed for self-signed certs)
            KeyStore trustStore = KeyStore.getInstance("BKS"); 
            // Load the truststore from a file or other source
            // ... 

            // Create a TrustManager that uses your truststore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Create an SSLContext that uses your TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Build the OkHttpClient with your custom SSLContext
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), 
                                      (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                    .build();

            // Make your API request
            Request request = new Request.Builder()
                    .url("https://your-api-endpoint.com")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) 
                    throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            } catch (IOException e) {
                // Handle potential certificate or network errors gracefully
                e.printStackTrace();
                // Inform the user about the issue
            }

        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}