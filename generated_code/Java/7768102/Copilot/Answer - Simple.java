import java.io.*;
import java.net.*;

public class FileTransfer {

    public static void main(String[] args) {
        // Start the server in a new thread
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(9090);
                System.out.println("Server is running and waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                // Receive file from client
                InputStream in = clientSocket.getInputStream();
                FileOutputStream fileOut = new FileOutputStream("received_file.txt");
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                }

                fileOut.close();
                clientSocket.close();
                serverSocket.close();
                System.out.println("File received successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Start the client
        try {
            Socket socket = new Socket("localhost", 9090);
            System.out.println("Connected to server!");

            // Send file to server
            FileInputStream fileIn = new FileInputStream("file_to_send.txt");
            OutputStream out = socket.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            fileIn.close();
            socket.close();
            System.out.println("File sent successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
