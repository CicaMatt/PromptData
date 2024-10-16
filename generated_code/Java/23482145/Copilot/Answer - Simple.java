import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class UniqueIdentifierGenerator {

    public static String generateUniqueIdentifier(String email) {
        try {
            // Get the current timestamp in milliseconds
            long timestamp = Instant.now().toEpochMilli();

            // Concatenate email and timestamp
            String input = email + timestamp;

            // Use SHA-256 for hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Encode the hash to base64 to get a readable alphanumeric string
            String base64Hash = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

            // Return the first 11 characters of the hash
            return base64Hash.substring(0, 11);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating unique identifier", e);
        }
    }

    public static void main(String[] args) {
        String email = "user@example.com";
        String uniqueId = generateUniqueIdentifier(email);
        System.out.println("Unique Identifier: " + uniqueId);
    }
}
