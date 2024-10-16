import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "path/to/your/file.txt";
        String text = "Hello, world!";

        try (FileWriter writer = new FileWriter(new File(path), Charset.forName("UTF-8"))) {
            writer.write(text);
        }
    }
}