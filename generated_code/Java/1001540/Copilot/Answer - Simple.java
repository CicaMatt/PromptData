import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "your_file_path_here";
        String src = "your_text_here";

        // Create a FileOutputStream with the specified path
        FileOutputStream fos = new FileOutputStream(new File(path));
        // Create an OutputStreamWriter with UTF-8 encoding
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        // Write the text to the file
        osw.write(src);
        // Flush and close the writer
        osw.flush();
        osw.close();
    }
}
