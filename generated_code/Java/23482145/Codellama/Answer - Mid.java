import java.util.Random;

public class UniqueIdentifier {
    public static void main(String[] args) {
        // Generate a random alphanumeric string of length 11
        String uniqueId = generateUniqueId();
        System.out.println("Generated unique identifier: " + uniqueId);
    }

    private static String generateUniqueId() {
        return new BigInteger(130, new Random()).toString(36).substring(0, 11);
    }
}