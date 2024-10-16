import java.net.HttpURLConnection;
import java.net.URL;



public class Main {
    void sendRequest(String request, String httpMethod) {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the HTTP request method based on the value of the "httpMethod" parameter
        if ("GET".equalsIgnoreCase(httpMethod)) {
            connection.setRequestMethod("GET");
        } else if ("POST".equalsIgnoreCase(httpMethod)) {
            connection.setRequestMethod("POST");
        } else {
            throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
        }

        // Set other request properties as needed
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("charset", "utf-8");

        // Connect to the server and send the request
        connection.connect();
    }
}