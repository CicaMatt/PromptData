import java.security.MessageDigest;
import java.util.Base64;

public class PasswordHashManager {
    private static final String _salt = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int _hashIterationsMax = 10;

    public static String getPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] hashData = plaintextPassword.getBytes("UTF-16LE");
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(hashData);
        byte messageDigest[] = digest.digest();

        // Convert the hash to a base64 string
        String encodedHash = Base64.getEncoder().encodeToString(messageDigest);

        return encodedHash;
    }

    public static boolean verifyPasswordHash(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Get the hash of the plaintext password
        byte[] hashData = plaintextPassword.getBytes("UTF-16LE");
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(hashData);
        byte messageDigest[] = digest.digest();

        // Convert the encrypted password to a base64 string
        String encodedEncryptedPassword = Base64.getDecoder().decode(encryptedPassword);

        // Compare the hashes
        return Arrays.equals(messageDigest, encodedEncryptedPassword.getBytes("UTF-16LE"));
    }
}