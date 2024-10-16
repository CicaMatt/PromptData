import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Utf8FileWriter {

    public static void writeToFile(String path, String src) {
        // Validate input for security and robustness
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        if (src == null) {
            src = ""; // Ensure we have a valid string, even if the input is null
        }

        File file = new File(path);
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            out.write(src, 0, src.length());
            out.flush();
        } catch (Exception e) {
            // Log error (you can replace this with a proper logging framework)
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage
        String path = "output.txt"; // Provide your file path here
        String src = "This is a test string for UTF-8 encoding.";
        
        writeToFile(path, src);
        System.out.println("File written successfully in UTF-8.");
    }
}
