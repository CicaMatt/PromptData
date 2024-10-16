import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class FileUploader {

    public static void main(String[] args) throws IOException {
        String urlToConnect = "http://localhost:9000/upload";
        File fileToUpload = new File("C:\\Users\\joao\\Pictures\\bla.jpg");

        Random random = new Random();
        int randomInt = random.nextInt(1 << 24); 
        String boundary = String.format("%08x", randomInt); 

        URLConnection connection = new URL(urlToConnect).openConnection();
        connection.setDoOutput(true); 
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"picture\"; filename=\"bla.jpg\"");
            writer.println("Content-Type: image/jpeg");
            writer.println();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload)));
                for (String line; (line = reader.readLine()) != null;) {
                    writer.println(line);
                }
            } finally {
                if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            }
            writer.println("--" + boundary + "--");
        } finally {
            if (writer != null) writer.close();
        }
    }
}
