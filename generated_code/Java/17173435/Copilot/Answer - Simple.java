import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUploader {
    public static void main(String[] args) {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); 

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlToConnect).openConnection();
            connection.setDoOutput(true); 
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (OutputStream output = connection.getOutputStream();
                 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)) {

                // Send binary file.
                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"")
                    .append(fileToUpload.getName()).append("\"\r\n");
                writer.append("Content-Type: ").append("image/jpeg").append("\r\n");
                writer.append("\r\n").flush();

                try (InputStream input = new FileInputStream(fileToUpload)) {
                    byte[] buffer = new byte[1024];
                    for (int length; (length = input.read(buffer)) > 0; ) {
                        output.write(buffer, 0, length);
                    }
                    output.flush(); 
                }

                writer.append("\r\n").flush();
                writer.append("--").append(boundary).append("--").append("\r\n").flush();
            }

            // Connection is lazily executed whenever you request any status.
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
