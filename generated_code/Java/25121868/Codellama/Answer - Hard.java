import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CMSHashManager {
    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS_MAX = 10;

    // Method to hash a password
    public static String getPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException {
        byte[] hashData = generateHash(plaintextPassword, SALT, HASH_ITERATIONS_MAX);
        return Base64.getEncoder().encodeToString(hashData);
    }

    // Method to verify a hashed password
    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException {
        byte[] hashData = generateHash(plaintextPassword, SALT, HASH_ITERATIONS_MAX);
        String computedHash = Base64.getEncoder().encodeToString(hashData);
        return encryptedPassword.equals(computedHash);
    }

    // Private method to handle the hashing logic
    private static byte[] generateHash(String plaintextPassword, String salt, int iterations) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = (plaintextPassword + salt).getBytes(StandardCharsets.UTF_16LE);
        
        for (int i = 0; i < iterations; i++) {
            hashData = digest.digest(hashData);
        }
        return hashData;
    }
}
