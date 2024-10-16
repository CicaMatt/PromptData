import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.methods.HttpGet;

public class GetPublicKey {
    public static void main(String[] args) throws Exception {
        // Initialize the URL of the website you want to get the public key from
        String url = "https://www.example.com/";

        // Create a new HttpClient and set the connection timeout to 5 seconds
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(5, TimeUnit.SECONDS).build();

        // Construct an HTTP GET request for the website's SSL certificate
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36");

        // Execute the HTTP GET request and get the response
        HttpResponse response = httpClient.execute(getRequest);

        // Get the SSL certificate from the response
        X509Certificate cert = (X509Certificate) response.getFirstHeader("ssl-certificate");

        // Print the public key of the SSL certificate
        System.out.println(cert.getPublicKey());
    }
}