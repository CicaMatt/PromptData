import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Main {
    public static void main(String[] args) {
        String path = "your_file_path_here";
        String src = "your_text_here";

        try {
            File file = new File(path);
            Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            String text = new String(src != null ? src : "");
            out.write(text, 0, text.length());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
