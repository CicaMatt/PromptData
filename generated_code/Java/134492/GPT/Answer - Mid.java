import java.io.*;
import java.util.Base64;
import java.util.LinkedList;

public class SerializeToString {

    public static void main(String[] args) {
        try {
            // Create a LinkedList object to serialize
            LinkedList<String> patches = new LinkedList<>();
            patches.add("Patch 1");
            patches.add("Patch 2");

            // Serialize to string
            String serializedObject = serializeObjectToString(patches);
            System.out.println("Serialized Object: " + serializedObject);

            // Deserialize from string
            LinkedList<String> deserializedObject = (LinkedList<String>) deserializeObjectFromString(serializedObject);
            System.out.println("Deserialized Object: " + deserializedObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to serialize an object to a Base64 encoded string
    public static String serializeObjectToString(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.close();

        // Encode the byte array to Base64 string to ensure it can be stored in a database
        return Base64.getEncoder().encodeToString(byteStream.toByteArray());
    }

    // Method to deserialize an object from a Base64 encoded string
    public static Object deserializeObjectFromString(String str) throws IOException, ClassNotFoundException {
        // Decode the Base64 string back to a byte array
        byte[] data = Base64.getDecoder().decode(str);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object obj = objectStream.readObject();
        objectStream.close();

        return obj;
    }
}
