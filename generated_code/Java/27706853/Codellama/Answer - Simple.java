import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyCustomTrustManager implements TrustManager, X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Implement your custom trust logic here.
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Implement your custom trust logic here.
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

public class MyHTTPSRequest {
    private static final String TAG = "MyHTTPSRequest";

    public void makeHTTPSRequest(String url, JSONObject params) throws IOException {
        URL requestUrl = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();

        // Set the custom trust manager for this HTTPS connection
        MyCustomTrustManager customTrustManager = new MyCustomTrustManager();
        connection.setSSLSocketFactory(customTrustManager.getSocketFactory());

        // Make the HTTPS request with the custom trust manager
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            Log.i(TAG, line);
        }
    }
}