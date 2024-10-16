import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    void sendRequest(String request, String httpMethod) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();

            switch (httpMethod.toUpperCase()) { // Switch for HTTP method
                case "GET":
                    connection.setRequestMethod("GET");
                    break;
                case "POST":
                    connection.setRequestMethod("POST");
                    break;
                default:
                    throw new IllegalArgumentException("Metodo HTTP non valido: " + httpMethod);
            }

            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");

            connection.connect();

        } catch (MalformedURLException e) {
            System.err.println("URL non valido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Errore nella connessione: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect(); // Disconnection
            }
        }
    }
}
