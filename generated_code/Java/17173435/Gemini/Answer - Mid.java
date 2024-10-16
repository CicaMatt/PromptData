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

        try (
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
            FileInputStream fileInputStream = new FileInputStream(fileToUpload)
        ) {
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"" 
                + fileToUpload.getName() + "\"").append("\r\n");
            writer.append("Content-Type: image/jpeg").append("\r\n"); 
            writer.append("\r\n").flush();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush(); 

            writer.append("\r\n");
            writer.append("--" + boundary + "--").append("\r\n").flush();
        }

        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); 
    }
}