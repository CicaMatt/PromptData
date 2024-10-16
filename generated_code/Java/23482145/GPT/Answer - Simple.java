import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class UniqueIdentifierGenerator {

    public static String generateUniqueId(String email) {
        try {
            // Get current timestamp in seconds
            long timestamp = Instant.now().getEpochSecond();
            
            // Combine email and timestamp
            String input = email + timestamp;

            // Use SHA-256 for hashing (or any other hash algorithm)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert hash to alphanumeric string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Limit the result to 11 characters
            return hexString.toString().substring(0, 11);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating unique ID", e);
        }
    }

    public static void main(String[] args) {
        String email = "user@example.com";
        String uniqueId = generateUniqueId(email);
        System.out.println("Generated Unique ID: " + uniqueId);
    }
}
