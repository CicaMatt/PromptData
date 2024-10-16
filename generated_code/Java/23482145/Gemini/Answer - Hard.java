import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public class UniqueIdentifierGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    // Private constructor to prevent instantiation
    private UniqueIdentifierGenerator() {
        // Intentionally left empty
    }

    public static String generateUniqueIdentifier(String email, boolean includeTimestamp)
            throws HashingAlgorithmNotFoundException, SaltGenerationException {

        // 1. Prepare the input data
        String inputData = email;
        if (includeTimestamp) {
            inputData += Instant.now().toEpochMilli();
        }

        // 2. Add a salt for additional security
        byte[] salt;
        try {
            salt = new byte[16];
            secureRandom.nextBytes(salt);
        } catch (Exception e) {
            throw new SaltGenerationException("Error generating salt", e);
        }
        inputData += Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

        // 3. Hash the input data
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new HashingAlgorithmNotFoundException("SHA-256 algorithm not found", e);
        }
        byte[] hashBytes = digest.digest(inputData.getBytes(StandardCharsets.UTF_8));

        // 4. Encode the hash digest
        String encodedString = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

        // 5. Truncate if necessary
        if (encodedString.length() > 11) {
            encodedString = encodedString.substring(0, 11);
        }

        return encodedString;
    }

    // Custom Exception classes
    public static class HashingAlgorithmNotFoundException extends Exception {
        public HashingAlgorithmNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class SaltGenerationException extends Exception {
        public SaltGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
