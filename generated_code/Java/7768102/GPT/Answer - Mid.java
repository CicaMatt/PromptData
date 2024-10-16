import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                InputStream inputStream = socket.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream("received_file");

                byte[] buffer = new byte[4096];
                int bytesRead;
                
                // Read from the socket and write to the file
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                socket.close();
                System.out.println("File received and saved.");
            }

        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}

public class FileClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        File file = new File("file_to_send.txt");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the server");

            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            
            // Read the file and send it over the socket
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
        }
    }
}
