import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SSLConnectionExample {
  public static void main(String[] args) throws Exception {
    // Create a trust manager that trusts all certificates, for demonstration purposes only
    X509TrustManager trustManager = new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) {}
      
      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) {}
      
      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };
    
    // Create an SSL socket factory that trusts all certificates, for demonstration purposes only
    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLContext.getDefault().getSocketFactory();
    
    // Create a URL object for the website you want to connect to
    URL url = new URL("https://www.example.com");
    
    // Open an HTTPS connection to the website using the trust manager and socket factory
    HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
    httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
    httpsUrlConnection.setHostnameVerifier((hostname, session) -> true);
    
    // Retrieve the SSL/TLS certificate from the website
    X509Certificate[] certificates = (X509Certificate[]) httpsUrlConnection.getServerCertificates();
    
    // Print the public key of the certificate, for demonstration purposes only
    System.out.println(certificates[0].getPublicKey());
  }
}