import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

public class OkHttpSslExample {

    public static void main(String[] args) {
        try {
            // Initialize TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);
            TrustManager[] trustManagers = tmf.getTrustManagers();
            X509TrustManager origTrustManager = (X509TrustManager) trustManagers[0];

            // Create a custom TrustManager that reuses the original TrustManager
            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustManager.checkClientTrusted(certs, authType);
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustManager.checkServerTrusted(certs, authType);
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return origTrustManager.getAcceptedIssuers();
                        }
                    }
            };

            // Initialize SSLContext with the custom TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Build OkHttpClient with custom SSL configuration and HostnameVerifier
            new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true; // Disable Certificate Pinning (not recommended for production)
                        }
                    })
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) wrappedTrustManagers[0])
                    .build();

        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | java.security.KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
