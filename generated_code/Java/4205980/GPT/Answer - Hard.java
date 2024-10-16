import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestSender {

    public void sendRequest(String request, String httpMethod, String parameters) throws IOException {
        // Construct the URL
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method based on the input (GET or POST)
        connection.setRequestMethod(httpMethod.toUpperCase());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");

        // Handle GET and POST differently
        if ("POST".equalsIgnoreCase(httpMethod)) {
            // Enable output for POST requests
            connection.setDoOutput(true);

            // Send the parameters in the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = parameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        connection.connect();

        // You can handle response here if needed (e.g., reading input stream, handling errors)
    }
}
