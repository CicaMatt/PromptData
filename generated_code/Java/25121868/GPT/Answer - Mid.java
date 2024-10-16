import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CMSHashManager {
    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int HASH_ITERATIONS_MAX = 10;

    // Gets the salted hash value with predetermined iterations.
    public static String getPasswordHash(String plaintextPassword) 
        throws NoSuchAlgorithmException {
        String hashData = plaintextPassword;
        for (int hashLimit = 0; hashLimit < HASH_ITERATIONS_MAX; hashLimit++) {
            hashData = getHash(SALT + hashData);
        }
        return hashData;
    }

    // Verifies the hash
    public static boolean verifyHashedPassword(String plaintextPassword, String encryptedPassword) 
        throws NoSuchAlgorithmException {
        String hashData = getPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashData);
    }

    // Gets the hash value of the data using SHA-512
    private static String getHash(String unhashedData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashData = digest.digest(unhashedData.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashData);
    }

    // Test case
    public static void main(String[] args) {
        try {
            String plaintext = "12345";
            String encrypted = 
                "NgkuakH7UsCQwGHMQOhVXI3nW6M+1AtREY4Qx35osQo87p/whZIzy8cZU7+R7XnmyzgMzLWSvX+rTiWzfGTPsA==";

            // Generate hash for plaintext
            String hashedPassword = getPasswordHash(plaintext);

            // Print hashed password
            System.out.println("Generated Hash: " + hashedPassword);

            // Verify hashed password
            boolean isVerified = verifyHashedPassword(plaintext, encrypted);
            System.out.println("Password verified: " + isVerified);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
