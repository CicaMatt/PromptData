import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SecureApiClient {

    private static final String SERVER_URL = "https://your-api-server.com"; 
    private static final String CERTIFICATE_PIN_SHA256 = "sha256/your-certificate-pin-here"; 

    public static void main(String[] args) throws IOException {

        // Create a CertificatePinner with the server's certificate pin
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(SERVER_URL, CERTIFICATE_PIN_SHA256)
                .build();

        // Create an OkHttpClient with certificate pinning enabled
        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // Create a request
        Request request = new Request.Builder()
                .url(SERVER_URL + "/your-api-endpoint") // Replace with your actual API endpoint
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }
}