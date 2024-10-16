import java.io.*;
import java.util.LinkedList;

class DiffMatchPatch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Serialize the object to a string using ObjectOutputStream
        LinkedList<Diff_match_patch.Patch> patches = // whatever...
        FileOutputStream fileStream = new FileOutputStream("foo.ser");
        ObjectOutputStream os = new ObjectOutputStream(fileStream);
        os.writeObject(patches);
        os.close();

        // Deserialize the string from the database using ObjectInputStream
        FileInputStream fileInputStream = new FileInputStream("foo.ser");
        ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
        LinkedList<Diff_match_patch.Patch> patches3 = (LinkedList<Diff_match_patch.Patch>) oInputStream.readObject();
        os.close();
    }
}