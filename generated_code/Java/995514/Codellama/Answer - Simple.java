import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HTTPSPostExample {
    public static void main(String[] args) throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = new HttpClient();

        // Set the URL for the request
        String url = "https://www.example.com/api/data";

        // Set the HTTP method to POST
        PostMethod postMethod = new PostMethod(url);

        // Set the headers for the request
        postMethod.setRequestHeader("Content-Type", "application/json");
        postMethod.setRequestHeader("Authorization", "Bearer YOUR_ACCESS_TOKEN");

        // Set the parameters for the request
        HttpMethodParams params = postMethod.getParams();
        params.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        // Add the request body to the method
        String jsonBody = "{\"key\": \"value\"}";
        postMethod.setRequestEntity(new StringRequestEntity(jsonBody, "application/json", "UTF-8"));

        // Execute the request
        httpClient.executeMethod(postMethod);

        // Read the response from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}