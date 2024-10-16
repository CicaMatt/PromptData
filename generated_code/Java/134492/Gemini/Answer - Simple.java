import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.LinkedList;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Patch;

public class Serializer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LinkedList<Patch> patches = new LinkedList<>(); // Initialize patches

        // Serialize to String
        String serializedPatches = serializeToString(patches);

        // Store serializedPatches in your database

        // Deserialize from String
        LinkedList<Patch> deserializedPatches = deserializeFromString(serializedPatches);
    }

    public static String serializeToString(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static <T> T deserializeFromString(String serializedObject) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(serializedObject);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        T object = (T) ois.readObject();
        ois.close();
        return object;
    }
}