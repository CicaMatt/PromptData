import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryption {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    public static void main(String[] args) throws Exception {
        // Generate a new AES key
        SecretKey secretKey = generateKey();

        // Encrypt data
        String originalData = "Sensitive data to be encrypted";
        String encryptedData = encrypt(originalData, secretKey);
        System.out.println("Encrypted Data: " + encryptedData);

        // Decrypt data
        String decryptedData = decrypt(encryptedData, secretKey);
        System.out.println("Decrypted Data: " + decryptedData);
    }

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] encryptedDataWithIv = new byte[IV_LENGTH_BYTE + encryptedData.length];
        System.arraycopy(iv, 0, encryptedDataWithIv, 0, IV_LENGTH_BYTE);
        System.arraycopy(encryptedData, 0, encryptedDataWithIv, IV_LENGTH_BYTE, encryptedData.length);
        return Base64.getEncoder().encodeToString(encryptedDataWithIv);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] encryptedDataWithIv = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        System.arraycopy(encryptedDataWithIv, 0, iv, 0, IV_LENGTH_BYTE);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] originalData = cipher.doFinal(encryptedDataWithIv, IV_LENGTH_BYTE, encryptedDataWithIv.length - IV_LENGTH_BYTE);
        return new String(originalData);
    }
}
