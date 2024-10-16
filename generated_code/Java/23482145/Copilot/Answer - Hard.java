import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UniqueIdentifierGenerator {

    // Method to generate unique identifier using email and current timestamp
    public static String generateUniqueIdentifier(String email) throws NoSuchAlgorithmException {
        // Get current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Combine email and timestamp to generate a unique input
        String input = email + timestamp;

        // Use SHA-256 to hash the input
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Encode the hash using Base64 and truncate to 11 characters
        String encodedHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

        // Return the first 11 characters (or less if the string is shorter)
        return encodedHash.length() > 11 ? encodedHash.substring(0, 11) : encodedHash;
    }

    public static void main(String[] args) {
        try {
            // Example email
            String email = "user@example.com";

            // Generate and print the unique identifier
            String uniqueId = generateUniqueIdentifier(email);
            System.out.println("Unique Identifier: " + uniqueId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
