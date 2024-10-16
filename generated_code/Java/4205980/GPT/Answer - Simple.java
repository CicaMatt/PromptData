import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    void sendRequest(String request, String params, String httpMethod) throws IOException {
        // Create the URL object
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method
        connection.setRequestMethod(httpMethod);
        connection.setInstanceFollowRedirects(false);

        // Set common properties
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");

        // If the method is POST, we need to send the parameters in the request body
        if ("POST".equalsIgnoreCase(httpMethod)) {
            connection.setDoOutput(true); 
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        // If the method is GET, the parameters should be part of the URL (already handled)
        connection.connect();
    }

}