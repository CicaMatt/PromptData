import java.io.*;
import java.net.*;

public class FileSender {
  public static void main(String[] args) throws IOException {
    // Create a socket to connect with the server
    Socket socket = new Socket("server_ip", server_port);

    // Get the input stream of the socket
    InputStream in = socket.getInputStream();

    // Get the output stream of the socket
    OutputStream out = socket.getOutputStream();

    // Send the file to the server using TCP
    File file = new File("path/to/file");
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) > 0) {
      out.write(buffer, 0, bytesRead);
    }

    // Close the socket
    socket.close();
  }
}