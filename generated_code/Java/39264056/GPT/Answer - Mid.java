import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.util.concurrent.TimeUnit;
import java.security.cert.X509Certificate;

public class SecureHttpClient {

    public static void main(String[] args) {
        OkHttpClient client = getSecureHttpClient();
        
        Request request = new Request.Builder()
                .url("https://your-secure-api.com/endpoint")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OkHttpClient getSecureHttpClient() {
        try {
            // Load CAs from an InputStream (could be from a resource or file)
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null); 

            // Set up a TrustManager that trusts the CAs in the KeyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Create an SSLContext with our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());

            // Create the OkHttpClient with the SSL context
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), 
                    (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // Verify the hostname (optional if certificate pinning is done)
                            return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                        }
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            return builder.build();
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create secure HTTP client", e);
        }
    }
}
