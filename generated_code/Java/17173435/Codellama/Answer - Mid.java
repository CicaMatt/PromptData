import java.io.*;
import java.net.*;

public class ImageUpload {
    public static void main(String[] args) {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); 

        URLConnection connection = new URL(urlToConnect).openConnection();
        connection.setDoOutput(true); 
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=picture; filename=bla.jpg");
            writer.println("Content-Type: image/jpeg");
            writer.println();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                for (String line; (line = reader.readLine()) != null;) {
                    writer.println(line);
                }
            } finally {
                if (reader != null) try {
                    reader.close();
                } catch (IOException logOrIgnore) {}
            }
            writer.println("--" + boundary + "--");
        } finally {
            if (writer != null) writer.close();
        }

        // Connection is lazily executed whenever you request any status.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); 
    }
}