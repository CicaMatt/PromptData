import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class CrossDatabaseEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; 
    private static final int KEY_SIZE = 256;

    // Convert your existing key to a byte array (ensure it's 32 bytes for AES-256)
    private static byte[] getSecretKeyBytes(String key) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            return sha.digest(keyBytes); 
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(getSecretKeyBytes(key), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    public static String decrypt(String encryptedData, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(getSecretKeyBytes(key), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}