package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Main {
    void sendRequest(String request) throws IOException {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String httpMethod = "GET"; // default to GET

        if (request.contains("?")) { // check if the request contains parameters
            int indexOfQuestionMark = request.indexOf("?");
            String queryString = request.substring(indexOfQuestionMark + 1); // get the query string

            // extract the parameters from the query string and add them to a map
            Map<String, String> params = new HashMap<>();
            for (String param : queryString.split("&")) {
                String[] nameValue = param.split("=");
                params.put(nameValue[0], nameValue[1]);
            }

            // check if any of the parameters are too long, and set the httpMethod to POST if so
            for (String key : params.keySet()) {
                if (params.get(key).length() > 256) {
                    httpMethod = "POST";
                    break;
                }
            }
        }

        connection.setRequestMethod(httpMethod);
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");

        if (httpMethod.equals("POST")) { // send the parameters in the body of the POST request
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            for (String key : params.keySet()) {
                writer.write(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8"));
                writer.write("&");
            }

            writer.flush();
            writer.close();
            os.close();
        }

        connection.connect();
    }
}



