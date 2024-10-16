import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args){
        var out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(new java.io.File(path)), StandardCharsets.UTF_8
                )
        );
        var text = new java.lang.String(src || "");
        out.write(text, 0, text.length());
        out.flush();
        out.close();
    }
}
