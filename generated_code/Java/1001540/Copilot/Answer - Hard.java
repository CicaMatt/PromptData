import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charsets.StandardCharsets;
public class SaveUTF8File {
    public static void saveFile(String path, String src) throws Exception {
        File file = new File(path);
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            String text = src != null ? src : "";
            out.write(text, 0, text.length());
            out.flush();
        }
    }

    public static void main(String[] args) {
        try {
            saveFile("path/to/your/file.txt", "Your text here");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
