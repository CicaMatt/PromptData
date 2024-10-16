import java.io.*;

public class Main {
    public static void main(String[] args){
        var out = new java.io.FileWriter(new java.io.File(path), "UTF-8");
        var text = new java.lang.String(src || "");
        out.write(text, 0, text.length());
        out.flush();
        out.close();
    }
}