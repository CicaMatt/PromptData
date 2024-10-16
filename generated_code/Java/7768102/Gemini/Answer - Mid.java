import java.io.*;
import java.net.Socket;

public class FileSender {

    public static void main(String[] args) {
        String serverAddress = "your_server_address"; // Replace with your server's address
        int serverPort = 12345; // Replace with your server's port
        String filePath = "path/to/your/file.txt"; // Replace with the path to your file

        try (Socket socket = new Socket(serverAddress, serverPort);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             OutputStream outputStream = socket.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully!");

        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
}