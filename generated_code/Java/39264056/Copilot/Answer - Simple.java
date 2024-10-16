import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SecureHttpClient {

    public static void main(String[] args) {
        try {
            OkHttpClient client = getSecureOkHttpClient();
            Request request = new Request.Builder()
                    .url("https://your-api-endpoint.com")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OkHttpClient getSecureOkHttpClient() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        final X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .build();
    }
}
