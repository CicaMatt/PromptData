import java.net.*;
import java.io.*;

public class FileTransferClient {
    public static void main(String[] args) throws IOException {
        // Set up the socket and streams
        Socket socket = new Socket("localhost", 8080);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
        // Send file size to server
        int fileSize = (int) new File("example.txt").length();
        out.writeInt(fileSize);
        
        // Send file contents to server
        byte[] buffer = new byte[1024];
        int bytesRead;
        FileInputStream fileStream = new FileInputStream("example.txt");
        while ((bytesRead = fileStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        
        // Close the socket and streams
        in.close();
        out.close();
        fileStream.close();
        socket.close();
    }
}

public class FileTransferServer {
    public static void main(String[] args) throws IOException {
        // Set up the socket and streams
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        
        // Read file size from client
        int fileSize = in.readInt();
        byte[] buffer = new byte[fileSize];
        
        // Read file contents from client
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        
        // Write the received file to disk
        FileOutputStream fileStream = new FileOutputStream("received.txt");
        fileStream.write(buffer);
        fileStream.close();
        
        // Close the socket and streams
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}