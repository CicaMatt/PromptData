import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Main {
    public void sendRequest(String baseUrl, String params, String httpMethod) throws IOException {
        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(httpMethod.toUpperCase());

        if (httpMethod.equalsIgnoreCase("POST")) {
            // For POST, we need to send parameters in the body of the request
            connection.setDoOutput(true); // Enable output stream for sending data
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            // Writing the POST parameters to the output stream
            try (OutputStream os = connection.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                writer.write(params);
                writer.flush();
            }
        } else if (httpMethod.equalsIgnoreCase("GET")) {
            // For GET, append the parameters to the URL
            url = new URL(baseUrl + "?" + params);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        }

        // Finally, connect (this actually sends the request)
        connection.connect();

        // Handle the response (this part can be extended as needed)
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }

}