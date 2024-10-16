import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class ImageUploader {

    public static void main(String[] args) {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); 
        String line_feed = "\r\n";
        
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        PrintWriter writer = null;

        try {
            // Open connection
            URL url = new URL(urlToConnect);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Get output stream
            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // Write boundary and headers for the file part
            writer.append("--").append(boundary).append(line_feed);
            writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"bla.jpg\"").append(line_feed);
            // Automatically detect the file type
            writer.append("Content-Type: ").append(Files.probeContentType(fileToUpload.toPath())).append(line_feed); 
            writer.append(line_feed).flush();

            // Write the binary file data
            FileInputStream inputStream = new FileInputStream(fileToUpload);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            // End of multipart/form-data
            writer.append(line_feed).flush();
            writer.append("--").append(boundary).append("--").append(line_feed).flush();

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Server returned: " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (outputStream != null) outputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
