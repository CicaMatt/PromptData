import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecureHashManager {

    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS_MAX = 10;

    // Hash the password with salt and multiple iterations
    public static String getPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException {
        String hashData = plaintextPassword;
        for (int hashLimit = 0; hashLimit < HASH_ITERATIONS_MAX; hashLimit++) {
            hashData = getHash(SALT + hashData);
        }
        return hashData;
    }

    // Verify the hashed password
    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException {
        String hashData = getPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashData);
    }

    // Internal hash function using SHA-512
    private static String getHash(String unhashedData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(unhashedData.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashData);
    }
}