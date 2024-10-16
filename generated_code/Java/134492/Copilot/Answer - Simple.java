import java.io.*;
import java.util.Base64;
import java.util.LinkedList;

public class SerializeToString {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LinkedList<Diff_match_patch.Patch> patches = new LinkedList<>(); // your object

        // Serialize to String
        String serializedObject = serializeToString(patches);
        System.out.println("Serialized object: " + serializedObject);

        // Deserialize from String
        LinkedList<Diff_match_patch.Patch> deserializedPatches = (LinkedList<Diff_match_patch.Patch>) deserializeFromString(serializedObject);
        System.out.println("Deserialized object: " + deserializedPatches);
    }

    private static String serializeToString(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    private static Object deserializeFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}
