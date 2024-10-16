import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        // Connect to a server using the TCP protocol
        Socket socket = new Socket("localhost", 8080);

        // Send data over the connection
        OutputStream out = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);

        writer.println("Hello, Server!");

        // Close the connection
        socket.close();
    }
}