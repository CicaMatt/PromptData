import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256Encryption {

    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final int AES_KEY_SIZE = 256;

    // Generate a new AES key (this can be stored securely and reused)
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(AES_KEY_SIZE); 
        return keyGenerator.generateKey();
    }

    // Generate a random IV (this should be stored alongside the ciphertext)
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; 
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Encrypt a plaintext string using AES-256-CBC
    public static String encrypt(String plainText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes); 
    }

    // Decrypt an encrypted string using AES-256-CBC
    public static String decrypt(String encryptedText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, "UTF-8");
    }

    // Convert a SecretKey to a byte array for storage
    public static byte[] keyToBytes(SecretKey secretKey) {
        return secretKey.getEncoded();
    }

    // Convert a byte array back to a SecretKey
    public static SecretKey bytesToKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, AES);
    }

    public static void main(String[] args) {
        // Example usage:

        // 1. Generate AES key and IV
        SecretKey secretKey = generateAESKey();
        byte[] iv = generateIV();

        // 2. Encrypt a plaintext string
        String plainText = "This is a sensitive data";
        String encryptedText = encrypt(plainText, secretKey, iv);
        System.out.println("Encrypted: " + encryptedText);

        // 3. Decrypt the encrypted string
        String decryptedText = decrypt(encryptedText, secretKey, iv);
        System.out.println("Decrypted: " + decryptedText);

        // You can store encryptedText, keyToBytes(secretKey), and iv in your database
    }
}
