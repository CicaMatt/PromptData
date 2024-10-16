import java.io.*;
import java.util.Base64;
import java.util.LinkedList;

public class SerializeToString {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Example LinkedList of Diff_match_patch.Patch (replace this with your actual LinkedList)
        LinkedList<Diff_match_patch.Patch> patches = new LinkedList<>();
        // Add sample patch objects to the list
        patches.add(new Diff_match_patch.Patch());

        // Serialize to a string
        String serializedString = serializeObjectToString(patches);
        System.out.println("Serialized String: " + serializedString);

        // Deserialize back from the string
        LinkedList<Diff_match_patch.Patch> deserializedPatches = (LinkedList<Diff_match_patch.Patch>) deserializeObjectFromString(serializedString);
        System.out.println("Deserialized Object: " + deserializedPatches);
    }

    // Serialize object to a Base64 string
    public static String serializeObjectToString(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    // Deserialize object from a Base64 string
    public static Object deserializeObjectFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    // Dummy class for Diff_match_patch.Patch, replace this with the actual one
    public static class Diff_match_patch {
        public static class Patch implements Serializable {
            // Your patch data here
        }
    }
}
