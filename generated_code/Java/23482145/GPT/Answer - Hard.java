import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SecureUniqueIdentifierGenerator {

    // Configurable parameters for security
    private static final int SALT_LENGTH = 16; 
    private static final int HASH_ITERATIONS = 10000; 
    private static final int HASH_LENGTH = 256; 

    // Function to generate a secure unique identifier
    public static String generateSecureUniqueId(String email) throws HashingException, SaltGenerationException {
        try {
            // Generate a cryptographically secure random salt
            byte[] salt = generateSalt();

            // Derive a key using PBKDF2 with HMAC-SHA256
            byte[] hash = pbkdf2Hash(email, salt);

            // Encode the result in Base64 for readability
            String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);

            // Truncate the encoded string to 11 characters
            return encoded.substring(0, Math.min(11, encoded.length()));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Throw a dedicated exception when a hashing error occurs
            throw new HashingException("Error generating secure unique identifier during hashing process", e);
        }
    }

    // Function to generate a cryptographically secure salt
    private static byte[] generateSalt() throws SaltGenerationException {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            // Throw a dedicated exception when salt generation fails
            throw new SaltGenerationException("Error generating cryptographically secure salt", e);
        }
    }

    // Function to hash the email with PBKDF2 (Password-Based Key Derivation Function 2)
    private static byte[] pbkdf2Hash(String email, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(email.toCharArray(), salt, HASH_ITERATIONS, HASH_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    public static void main(String[] args) {
        // Test with an example email
        String email = "user@example.com";
        try {
            String uniqueId = generateSecureUniqueId(email);
            System.out.println("Generated Secure Unique ID: " + uniqueId);
        } catch (HashingException | SaltGenerationException e) {
            // Handle custom exceptions in a meaningful way
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
