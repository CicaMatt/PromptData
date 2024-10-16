import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Main {
    void sendRequest(String request, String httpMethod) {
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        if (httpMethod.equals("GET")) {
            connection.setRequestMethod("GET");
        } else if (httpMethod.equals("POST")) {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            String parameters = "";
            for (String param : request.split("&")) {
                if (param.contains("=")) {
                    String[] keyValue = param.split("=");
                    parameters += URLEncoder.encode(keyValue[0], "UTF-8") + "=" + URLEncoder.encode(keyValue[1], "UTF-8") + "&";
                } else {
                    parameters += URLEncoder.encode(param, "UTF-8") + "&";
                }
            }
            writer.write(parameters);
            writer.flush();
        }
    }

}



