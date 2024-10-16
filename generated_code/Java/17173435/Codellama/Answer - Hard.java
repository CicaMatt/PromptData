import java.io.*;
import java.net.*;

public class ImageSender {
    public static void main(String[] args) throws Exception {
        // URL to connect to
        String urlToConnect = "http://localhost:9000/upload";

        // File to upload
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");

        // Create a boundary for the HTTP request
        String boundary = Long.toHexString(System.currentTimeMillis());
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        // Open a connection to the URL and set the request method to POST
        HttpURLConnection connection = (HttpURLConnection) new HttpURLConnection(urlToConnect).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        // Set the Content-Type header for the HTTP request
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Create an OutputStream to write to the connection
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

        // Write the initial boundary string to the output stream
        writer.append(twoHyphens + boundary).append(lineEnd);
        writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"" + fileToUpload.getName() + "\"")
              .append(lineEnd);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileToUpload.getName()))
              .append(lineEnd);
        writer.append(lineEnd).flush();

        // Write the binary data of the file
        FileInputStream fileInputStream = new FileInputStream(fileToUpload);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        fileInputStream.close();

        // Write the final boundary string
        writer.append(lineEnd).flush();
        writer.append(twoHyphens + boundary + twoHyphens).append(lineEnd).flush();

        // Close the writer
        writer.close();

        // Get the response code from the HttpURLConnection
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read and print the response from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            System.out.println(responseLine);
        }
        reader.close();
    }
}
