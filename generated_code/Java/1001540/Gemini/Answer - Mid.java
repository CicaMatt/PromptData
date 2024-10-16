import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UTF8FileWriter {

    public static void main(String[] args) {
        // Replace with your actual file path and text
        String path = "your_file_path.txt"; 
        String src = "Your text content here"; 

        try {
            // Specify UTF-8 encoding explicitly
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), StandardCharsets.UTF_8));

            String text = new String(src); 
            out.write(text);
            out.flush();
            out.close();

            System.out.println("File written successfully with UTF-8 encoding.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}