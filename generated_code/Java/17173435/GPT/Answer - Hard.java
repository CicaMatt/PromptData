import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class FileUploader {
    private static final String LINE_FEED = "\r\n";
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        String urlToConnect = "http://localhost:9000/upload";
        String jpgUrl = "C:\\Users\\joao\\Pictures\\bla.jpg";
        File fileToUpload = new File(jpgUrl);

        String boundary = Long.toHexString(System.currentTimeMillis());

        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        PrintWriter writer = null;

        try {
            connection = createConnection(urlToConnect, boundary);

            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharset.UTF_8), true);

            sendFile(writer, outputStream, fileToUpload, boundary);

            int responseCode = connection.getResponseCode();
            System.out.println("Server response code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(writer);
            closeQuietly(outputStream);
            if (connection != null) connection.disconnect();
        }
    }

    private static HttpURLConnection createConnection(String urlToConnect, String boundary) throws IOException {
        URL url = new URL(urlToConnect);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        return connection;
    }

    private static void sendFile(PrintWriter writer, OutputStream outputStream, File fileToUpload, String boundary) {
        String mimeType = Files.probeContentType(fileToUpload.toPath());

        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"")
              .append(fileToUpload.getName()).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(mimeType != null ? mimeType : "application/octet-stream")
              .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED).flush();

        try (FileInputStream inputStream = new FileInputStream(fileToUpload)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        writer.append(LINE_FEED).flush();
        writer.append("--").append(boundary).append("--").append(LINE_FEED).flush();
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                System.out.println("Exception catched")
            }
        }
    }
}
