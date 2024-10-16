import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CMSHashManager {

    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS_MAX = 10;

    private CMSHashManager() {
        // Private constructor to prevent instantiation
    }

    public static String getPasswordHash(String plaintextPassword) {
        String hashData = plaintextPassword;
        for (int hashLimit = 0; hashLimit < HASH_ITERATIONS_MAX; hashLimit++) {
            hashData = getHash(SALT + hashData);
        }
        return hashData;
    }

    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) {
        String hashData = getPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashData);
    }

    private static String getHash(String unhashedData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashData = digest.digest(unhashedData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashData);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception appropriately (e.g., log it, throw a custom exception)
            e.printStackTrace();
            return null; // Or another suitable default value
        }
    }
}