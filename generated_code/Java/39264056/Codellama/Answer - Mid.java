import android.content.Context;
import android.util.Log;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLUtil {
    private static final String TAG = "SSLUtil";

    private static SSLSocketFactory sslSocketFactory;

    public static SSLSocketFactory getSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        if (sslSocketFactory == null) {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // Initialise the TMF as you normally would, for example:
            try {
                tmf.init((KeyStore)null);
            } catch(KeyStoreException e) {
                Log.e(TAG, "Error initializing TrustManagerFactory", e);
            }
            TrustManager[] trustManagers = tmf.getTrustManagers();

            final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

            // Create a trust manager that does not validate certificate chains
            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return origTrustmanager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            try {
                                origTrustmanager.checkClientTrusted(certs, authType);
                            } catch(CertificateException e) {
                                Log.e(TAG, "Error checking client trust", e);
                            }
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            try {
                                origTrustmanager.checkServerTrusted(certs, authType);
                            } catch(CertificateException e) {
                                Log.e(TAG, "Error checking server trust", e);
                            }
                        }
                    }
            };
            // TrustedManager[] trustAllCerts = TrustManagerFactory.getInstance("SSL").getTrustManagers();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();
        }
        return sslSocketFactory;
    }
}