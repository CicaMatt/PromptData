import java.io.*;
import java.util.Base64;
import java.util.LinkedList;

// Assuming the use of an external library for diff_match_patch
import name.fraser.neil.plaintext.diff_match_patch;

public class SerializationExample {

    // Method to serialize an object to a Base64 string
    public static String serializeToString(LinkedList<diff_match_patch.Patch> patches) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(patches);  
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());  
        }
    }

    // Method to deserialize a Base64 string back to an object
    public static LinkedList<diff_match_patch.Patch> deserializeFromString(String serializedString) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(serializedString);  
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            Object deserializedObject = objectStream.readObject(); 
            if (deserializedObject instanceof LinkedList) {
                return (LinkedList<diff_match_patch.Patch>) deserializedObject;  
            } else {
                throw new IllegalArgumentException("Deserialized object is not of type LinkedList<diff_match_patch.Patch>");
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Example LinkedList of diff_match_patch.Patch
            LinkedList<diff_match_patch.Patch> patches = new LinkedList<>();
            patches.add(new diff_match_patch().new Patch());

            // Serialize to Base64 string
            String serializedData = serializeToString(patches);
            System.out.println("Serialized Data: " + serializedData);

            // Simulate storing into a database by storing in a string (instead of file)
            String storedData = serializedData;

            // Deserialize from Base64 string
            LinkedList<diff_match_patch.Patch> deserializedPatches = deserializeFromString(storedData);
            System.out.println("Deserialized Patches: " + deserializedPatches);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
