import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

void sendRequest(String request, String httpMethod) throws IOException {
    URL url = new URL(request);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod(httpMethod);

    if (httpMethod.equalsIgnoreCase("POST")) {
        // Crucial for POST: Enable output and set content type 
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Extract parameters from the URL
        String parameters = request.substring(request.indexOf("?") + 1);

        // Write parameters to the request body
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(parameters);
            wr.flush();
        }
    } else { // GET or other methods
        connection.setDoOutput(false); // Ensure no output for GET
    }

    connection.setInstanceFollowRedirects(false);
    connection.setRequestProperty("charset", "utf-8");
    connection.connect();

    // ... (rest of your code to handle the response)
}