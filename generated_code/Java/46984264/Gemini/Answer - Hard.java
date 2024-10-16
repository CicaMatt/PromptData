import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class CrossDatabaseEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;

    // Securely retrieve the encryption key (simulated)
    private static SecretKey getEncryptionKey() throws NoSuchAlgorithmException {
        // In production, retrieve this key from a secure location like AWS KMS or a Vault.
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE); // Initialize key generator for 256-bit key
        return keyGen.generateKey();
    }

    public static String encrypt(String plainText) throws Exception {
        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty");
        }

        // Generate a secure Initialization Vector (IV)
        byte[] iv = new byte[16];
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Retrieve encryption key securely
        SecretKeySpec secretKeySpec = new SecretKeySpec(getEncryptionKey().getEncoded(), "AES");

        // Create and initialize the cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Encrypt the data
        byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Combine the IV and encrypted data for storage or transmission
        byte[] combinedData = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combinedData, 0, iv.length);
        System.arraycopy(encryptedData, 0, combinedData, iv.length, encryptedData.length);

        // Encode the combined data using Base64 for safe storage and transmission
        return Base64.getEncoder().encodeToString(combinedData);
    }

    public static String decrypt(String cipherText) throws Exception {
        if (cipherText == null || cipherText.isEmpty()) {
            throw new IllegalArgumentException("Cipher text cannot be null or empty");
        }

        // Decode the Base64 encoded data
        byte[] combinedData = Base64.getDecoder().decode(cipherText);

        // Extract the IV and encrypted data from the combined data
        byte[] iv = new byte[16];
        byte[] encryptedData = new byte[combinedData.length - 16];
        System.arraycopy(combinedData, 0, iv, 0, 16);
        System.arraycopy(combinedData, 16, encryptedData, 0, encryptedData.length);

        // Create an IvParameterSpec from the extracted IV
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Retrieve encryption key securely
        SecretKeySpec secretKeySpec = new SecretKeySpec(getEncryptionKey().getEncoded(), "AES");

        // Create and initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Decrypt the data
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Convert the decrypted data back to a String
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String originalText = "Sensitive data to be encrypted";

        String encryptedText = encrypt(originalText);
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }
}
