import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class UniqueIdentifierGenerator {

    public static String generateUniqueIdentifier(String userEmail) {
        try {
            // Incorporate current timestamp for added uniqueness
            String dataToHash = userEmail + Instant.now().toEpochMilli();

            // Use SHA-256 for strong hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            // Base64 encode for alphanumeric representation
            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            // Truncate to 11 characters or less
            return base64Encoded.substring(0, Math.min(11, base64Encoded.length())); 

        } catch (NoSuchAlgorithmException e) {
            // Handle the unlikely scenario of SHA-256 being unavailable
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        String userEmail = "test@example.com";
        String uniqueId = generateUniqueIdentifier(userEmail);
        System.out.println("Generated Unique ID: " + uniqueId); 
    }
}