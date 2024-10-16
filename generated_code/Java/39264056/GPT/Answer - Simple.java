import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpSslExample {

    public static void main(String[] args) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(getSSLSocketFactory(), getTrustManager())
                    .build();

            Request request = new Request.Builder()
                    .url("https://your-secure-api.com")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                System.out.println(response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            final X509TrustManager origTrustManager = (X509TrustManager) trustManagers[0];

            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return origTrustManager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustManager.checkClientTrusted(certs, authType);
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustManager.checkServerTrusted(certs, authType);
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static X509TrustManager getTrustManager() {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
