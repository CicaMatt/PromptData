import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestSender {

    public void sendRequest(String request, String httpMethod) throws Exception {
        // i.e.: request = "http://example.com/index.php?param1=a&param2=b&param3=c";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(httpMethod);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");

        if ("POST".equalsIgnoreCase(httpMethod)) {
            connection.setDoOutput(true);
            String urlParameters = request.substring(request.indexOf("?") + 1);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = urlParameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        connection.connect();
    }
}
