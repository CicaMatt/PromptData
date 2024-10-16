import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class UniqueIdentifierGenerator {

    public static String generateUniqueIdentifier(String userEmail, boolean includeTimestamp) {
        try {
            String data = userEmail;
            if (includeTimestamp) {
                data += Instant.now().toEpochMilli();
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());

            String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            return base64Encoded.substring(0, Math.min(11, base64Encoded.length())); 

        } catch (NoSuchAlgorithmException e) {
            throw new IdentifierGenerationException("SHA-256 algorithm not available", e);
        }
    }

    public static void main(String[] args) {
        String userEmail = "test@example.com";
        String uniqueId = generateUniqueIdentifier(userEmail, true);
        System.out.println("Generated Unique ID: " + uniqueId); 
    }
}

class IdentifierGenerationException extends RuntimeException {
    public IdentifierGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
