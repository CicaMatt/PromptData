import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class TrustRootCertificates {
  public static void main(String[] args) throws Exception {
    // Create a trust manager factory that uses the default platform-specific algorithm
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    // Initialize the trust manager factory with the default trust store
    tmf.init((KeyStore) null);

    // Get the list of trusted root certificates from the trust manager
    X509Certificate[] trustedRoots = ((X509TrustManager) tmf.getTrustManagers()[0]).getAcceptedIssuers();

    System.out.println("Trusted Root Certificates:");
    for (X509Certificate root : trustedRoots) {
      System.out.println(root);
    }
  }
}