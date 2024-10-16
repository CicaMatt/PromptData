import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESEncryption {

    private static final String SECRET_KEY = "your-secret-key";

    // Generate a random 16-byte IV for AES encryption
    private static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Generate a random salt
    private static byte[] generateSalt() {
        byte[] salt = new byte[16]; 
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // Encrypt the input text using AES-256
    public static String encrypt(String strToEncrypt) {
        try {
            byte[] iv = generateIV();
            byte[] salt = generateSalt();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

            // Combine IV, salt, and encrypted text and Base64 encode it
            byte[] encryptedCombined = new byte[iv.length + salt.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedCombined, 0, iv.length);
            System.arraycopy(salt, 0, encryptedCombined, iv.length, salt.length);
            System.arraycopy(encrypted, 0, encryptedCombined, iv.length + salt.length, encrypted.length);

            return Base64.getEncoder().encodeToString(encryptedCombined);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Decrypt the encrypted string using AES-256
    public static String decrypt(String strToDecrypt) {
        try {
            byte[] decoded = Base64.getDecoder().decode(strToDecrypt);

            // Extract IV from the beginning of the decoded byte array
            byte[] iv = new byte[16];
            System.arraycopy(decoded, 0, iv, 0, iv.length);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Extract the salt from the decoded byte array
            byte[] salt = new byte[16];
            System.arraycopy(decoded, iv.length, salt, 0, salt.length);

            // Extract the encrypted data from the decoded byte array
            byte[] encryptedData = new byte[decoded.length - iv.length - salt.length];
            System.arraycopy(decoded, iv.length + salt.length, encryptedData, 0, encryptedData.length);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decrypted = cipher.doFinal(encryptedData);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String originalString = "SensitiveDataToEncrypt"; // String to encrypt

        String encryptedString = AESEncryption.encrypt(originalString);
        System.out.println("Encrypted: " + encryptedString);

        String decryptedString = AESEncryption.decrypt(encryptedString);
        System.out.println("Decrypted: " + decryptedString);
    }
}
