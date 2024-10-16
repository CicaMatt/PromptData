import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class FileSender {

    public static void main(String[] args) {
        try {
            // Create a Socket connected to the server
            Socket socket = new Socket("localhost", 1234); // Replace "localhost" with the server address and 1234 with the server port

            // Create a DataOutputStream to send data to the server
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // File to be sent
            File file = new File("path/to/your/file.txt"); // Replace with the actual file path

            // Get the file size
            long fileSize = file.length();

            // Send the file size to the server
            dataOutputStream.writeLong(fileSize);

            // Create a FileInputStream to read the file
            FileInputStream fileInputStream = new FileInputStream(file);

            // Buffer to read data in chunks
            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read data from the file and send it to the server
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams and socket
            fileInputStream.close();
            dataOutputStream.close();
            socket.close();

            System.out.println("File sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}