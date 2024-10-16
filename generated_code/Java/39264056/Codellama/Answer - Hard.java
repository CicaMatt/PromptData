import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLContextFactory {
    
    // Private constructor to prevent instantiation (SonarLint java:S1118)
    private SSLContextFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SSLSocketFactory createSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        
        try {
            tmf.init((KeyStore) null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        TrustManager[] trustManagers = tmf.getTrustManagers();
        final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];

        // Create a trust manager that does not validate certificate chains
        TrustManager[] wrappedTrustManagers = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return origTrustmanager.getAcceptedIssuers();
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try {
                        origTrustmanager.checkClientTrusted(certs, authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        origTrustmanager.checkServerTrusted(certs, authType);
                    } catch (CertificateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, new java.security.SecureRandom());

        // Return the ssl socket factory (SonarLint java:S1450 - declare as local)
        return sslContext.getSocketFactory();
    }
}
