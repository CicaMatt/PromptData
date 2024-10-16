import java.io.*;
import java.util.Base64;
import java.util.LinkedList;

public class SerializeToString {

    public static void main(String[] args) {
        try {
            // Example object to serialize
            LinkedList<String> patches = new LinkedList<>();
            patches.add("Patch1");
            patches.add("Patch2");

            // Serialize object to string
            String serializedObject = serializeObjectToString(patches);
            System.out.println("Serialized Object: " + serializedObject);

            // Deserialize object from string
            LinkedList<String> deserializedPatches = deserializeObjectFromString(serializedObject);
            System.out.println("Deserialized Object: " + deserializedPatches);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to serialize an object to a string
    public static String serializeObjectToString(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    // Method to deserialize an object from a string
    public static <T> T deserializeObjectFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        @SuppressWarnings("unchecked")
        T object = (T) objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}
