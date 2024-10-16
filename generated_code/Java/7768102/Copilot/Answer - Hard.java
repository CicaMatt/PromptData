import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {

            File file = new File("path/to/your/file.txt");
            FileInputStream fileInputStream = new FileInputStream(file);

            dataOutputStream.writeLong(file.length());
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            System.out.println("File sent successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class FileServer {
    private static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is listening on port " + SERVER_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new FileReceiver(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileReceiver implements Runnable {
    private Socket socket;

    public FileReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream("path/to/save/file.txt")) {

            long fileSize = dataInputStream.readLong();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while (fileSize > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                fileSize -= bytesRead;
            }

            System.out.println("File received successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
