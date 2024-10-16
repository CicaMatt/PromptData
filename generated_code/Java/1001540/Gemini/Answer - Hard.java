import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileSaver {

    public static void saveToFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8))) {
            writer.write(content);
        } 
    }

    public static void main(String[] args) {
        String path = "your_file_path.txt"; 
        String content = "Your text content here.";

        try {
            saveToFile(path, content);
            System.out.println("File saved successfully with UTF-8 encoding.");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}