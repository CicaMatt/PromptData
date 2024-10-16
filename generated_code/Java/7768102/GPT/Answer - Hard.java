import java.io.*;
import java.net.Socket;

public class FileClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FileClient <file_path>");
            return;
        }

        File file = new File(args[0]);

        if (!file.exists() || !file.isFile()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(file)) {

            // Send filename
            dos.writeUTF(file.getName());

            // Send file size
            dos.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalSent = 0;

            // Send file data to server
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
                totalSent += bytesRead;
            }

            System.out.println("File sent: " + file.getName() + " (" + totalSent + " bytes)");
        } catch (IOException e) {
            System.err.println("Error while sending file: " + e.getMessage());
        }
    }
}
