import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageUploader {
    public static void main(String[] args) throws IOException {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); 

        URLConnection connection = new URL(urlToConnect).openConnection();
        connection.setDoOutput(true); 
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream output = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

        // Send binary file.
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"bla.jpg\"").append("\r\n");
        writer.append("Content-Type: ")
            .append(URLConnection.guessContentTypeFromName(fileToUpload.getName())).append("\r\n");
        writer.append("Content-Transfer-Encoding: binary").append("\r\n");
        writer.append("\r\n").flush();

        FileInputStream input = new FileInputStream(fileToUpload);
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) > 0;) {
            output.write(buffer, 0, length);
        }
        output.flush(); 
        input.close();

        writer.append("\r\n").flush();
        writer.append("--").append(boundary).append("--").append("\r\n").close();

        // Connection is lazily executed whenever you request any status.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); 
    }
}
