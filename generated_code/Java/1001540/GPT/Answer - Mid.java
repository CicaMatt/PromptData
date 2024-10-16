import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class FileWriterUTF8 {
    public static void writeToFile(String path, String src) throws Exception {
        // Create the OutputStreamWriter and specify UTF-8 encoding
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8)) {
            String text = src != null ? src : "";
            out.write(text, 0, text.length());
            // Flush and close are handled automatically by the try-with-resources
        }
    }

    public static void main(String[] args) {
        try {
            writeToFile("path/to/your/file.txt", "Some content to write to the file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
