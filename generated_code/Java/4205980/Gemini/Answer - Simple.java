package org.example;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    void sendRequest(String request, String httpMethod) throws IOException {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Determine if we are using GET or POST
        boolean isPost = httpMethod.equalsIgnoreCase("POST");

        // Set the request method
        connection.setRequestMethod(httpMethod);

        // If POST, we need to set some additional properties
        if (isPost) {
            connection.setDoOutput(true); 
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // Set content type for form data
        }

        // Connect to the server
        connection.connect();

        // If POST, write the parameters to the output stream
        if (isPost) {
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                // Extract parameters from the request URL
                String parameters = request.substring(request.indexOf("?") + 1);
                writer.write(parameters);
                writer.flush();
            }
        }

        // Handle the response as needed (read from connection.getInputStream())
    }
}


