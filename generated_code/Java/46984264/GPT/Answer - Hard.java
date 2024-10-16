import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryptionUtil {

    // AES encryption mode: CBC with PKCS5 Padding
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE = 256;
    private static final int IV_SIZE = 16; 

    // Generates a random AES key (256 bits)
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, new SecureRandom());
        return keyGen.generateKey();
    }

    // Encrypt the input text using AES with a given key and IV
    public static String encrypt(String input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the encrypted text using AES with a given key and IV
    public static String decrypt(String encrypted, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Generates a random IV (Initialization Vector)
    public static byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    // Example usage
    public static void main(String[] args) {
        try {
            // Generate AES key and IV
            SecretKey key = generateAESKey();
            byte[] iv = generateIV();

            // Original message
            String originalMessage = "This is a secret message";

            // Encrypt the message
            String encryptedMessage = encrypt(originalMessage, key, iv);
            System.out.println("Encrypted Message: " + encryptedMessage);

            // Decrypt the message
            String decryptedMessage = decrypt(encryptedMessage, key, iv);
            System.out.println("Decrypted Message: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
