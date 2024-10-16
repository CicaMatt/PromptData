import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class FileUploader {

    public static void main(String[] args) throws IOException {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Unique random boundary value.
        String lineSeparator = "\r\n"; // CRLF line separator as per RFC 7578.

        // Open a connection to the server
        HttpURLConnection connection = (HttpURLConnection) new URL(urlToConnect).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)) {

            // Send the file metadata
            writer.append("--").append(boundary).append(lineSeparator);
            writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"").append(fileToUpload.getName()).append("\"").append(lineSeparator);
            writer.append("Content-Type: ").append(Files.probeContentType(fileToUpload.toPath())).append(lineSeparator); // e.g., image/jpeg
            writer.append("Content-Transfer-Encoding: binary").append(lineSeparator);
            writer.append(lineSeparator).flush();

            // Send the file content
            try (FileInputStream fileInputStream = new FileInputStream(fileToUpload)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush(); // Ensure all bytes are written out
            }

            // End of multipart/form-data.
            writer.append(lineSeparator).flush();
            writer.append("--").append(boundary).append("--").append(lineSeparator).flush();
        }

        // Check the server's response
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }
}
