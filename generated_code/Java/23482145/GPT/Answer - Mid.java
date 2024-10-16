import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class UniqueIDGenerator {

    public static String generateID(String userEmail) {
        try {
            // Generate current timestamp
            long currentTime = Instant.now().toEpochMilli();

            // Combine user email and timestamp into a single string
            String combinedInput = userEmail + currentTime;

            // Use SHA-256 to create a hash of the combined input
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = sha256Digest.digest(combinedInput.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed byte array into a Base64 encoded string (URL safe, no padding)
            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            // Return the first 11 characters from the Base64-encoded hash
            return base64Encoded.substring(0, 11);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while generating unique identifier", e);
        }
    }

    public static void main(String[] args) {
        String userEmail = "sample@example.com";
        String uniqueIdentifier = generateID(userEmail);
        System.out.println("Generated Unique ID: " + uniqueIdentifier);
    }
}
