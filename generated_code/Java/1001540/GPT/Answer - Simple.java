import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

var out = new BufferedWriter(
    new OutputStreamWriter(
        new FileOutputStream(new java.io.File(path)), StandardCharsets.UTF_8
    )
);
var text = new java.lang.String(src || ""); // Modify according to your needs
out.write(text, 0, text.length());
out.flush();
out.close();
