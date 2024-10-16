import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public static void main(String[] args) {
        int port = 5000; // Define a port number for communication

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            
            // Wait for a client to connect
            Socket socket = serverSocket.accept(); 
            System.out.println("Client connected");

            // Prepare to receive file data from the client
            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt");

            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read file data from the input stream and write it to a file
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File received successfully");

            fileOutputStream.close();
            socket.close(); // Close the connection

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

public class FileClient {

    public static void main(String[] args) {
        String serverAddress = "localhost"; 
        int port = 5000; 
        File file = new File("file_to_send.txt");

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to the server");

            // Prepare to send the file to the server
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            // Read the file and send it over the socket
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully");

            fileInputStream.close();
            socket.close(); // Close the connection

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
