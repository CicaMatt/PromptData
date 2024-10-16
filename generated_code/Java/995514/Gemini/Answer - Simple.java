import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostExample {

    public static void main(String[] args) throws Exception {
        String url = "http://example.com/api/endpoint"; // Example
        String data = "param1=value1&param2=value2";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set request method
        con.setRequestMethod("POST");

        // Add request headers
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));

        // Send request data
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(data.getBytes());
        os.flush();
        os.close();

        // Read response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }
}