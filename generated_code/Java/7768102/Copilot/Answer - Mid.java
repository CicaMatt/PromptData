import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 9000;
        String filePath = "path/to/your/file.txt";

        try (Socket socket = new Socket(serverAddress, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(new File(filePath))) {

            // Send file name and size
            File file = new File(filePath);
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            // Send file content
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class FileServer {
    public static void main(String[] args) {
        int port = 9000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(socket.getInputStream())) {

                    // Read file name and size
                    String fileName = dis.readUTF();
                    long fileSize = dis.readLong();

                    // Read file content and save it
                    try (FileOutputStream fos = new FileOutputStream("received_" + fileName)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        long totalBytesRead = 0;

                        while (totalBytesRead < fileSize && (bytesRead = dis.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                        }

                        System.out.println("File received successfully!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
