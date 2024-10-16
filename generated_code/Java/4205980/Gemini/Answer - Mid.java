import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {
    void sendRequest(String request, String httpMethod) throws IOException {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Determine the request method and set it
        connection.setRequestMethod(httpMethod);

        // Handle parameters based on the method
        if (httpMethod.equalsIgnoreCase("POST")) {
            connection.setDoOutput(true);
            String parameters = extractParametersFromRequest(request); // Extract parameters from the request URL

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = parameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        } else { // Default to GET if not explicitly POST
            connection.setDoOutput(false); // No output needed for GET
        }

        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.connect();

        // Handle the response (read from connection.getInputStream()) as needed
    }

    // Helper method to extract parameters from the request URL
    private String extractParametersFromRequest(String request) {
        int questionMarkIndex = request.indexOf('?');
        if (questionMarkIndex != -1) {
            return request.substring(questionMarkIndex + 1);
        } else {
            return "";
        }
    }

}