import java.io.*;
import java.net.Socket;

public class SecureFileSender {

    public static void main(String[] args) throws IOException {
        // Replace with your actual server address and port
        String serverAddress = "your_server_address"; 
        int port = 12345; 

        File fileToSend = new File("path/to/your/file"); // Replace with your file's path

        try (Socket socket = new Socket(serverAddress, port);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(fileToSend)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully!");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
}