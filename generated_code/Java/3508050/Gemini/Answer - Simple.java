import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TrustedRootCertificates {

    public static void main(String[] args) throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((javax.net.ssl.KeyStore) null); 

        for (javax.net.ssl.TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                X509TrustManager x509TrustManager = (X509TrustManager) trustManager;
                for (X509Certificate cert : x509TrustManager.getAcceptedIssuers()) {
                    System.out.println(cert.getSubjectDN()); 
                }
            }
        }
    }
}