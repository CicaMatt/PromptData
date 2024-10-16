import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDemo {

    public static void main(String[] args) throws Exception {
        // Retrieve password securely from environment variables
        String password = System.getenv("ENCRYPTION_PASSWORD");

        if (password == null || password.isEmpty()) {
            throw new Exception("Password is not set in environment variables");
        }

        // Securely generate a random 16-byte salt
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);

        // Increase the iteration count for added security
        int iterations = 10000;

        // Generate the secret key using PBKDF2-HMAC-SHA256
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt the message using AES-256 in CBC mode with NoPadding
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        // Generate a random IV (16 bytes for AES)
        byte[] iv = new byte[16];
        sr.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);

        // Define the plaintext and pad it manually to a multiple of 16 bytes (block size for AES)
        byte[] plainText = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/success&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name".getBytes(StandardCharsets.UTF_8);

        // Padding to 16 bytes if needed (PKCS7-style padding example)
        int blockSize = 16;
        int paddingSize = blockSize - (plainText.length % blockSize);
        byte[] paddedPlainText = new byte[plainText.length + paddingSize];
        System.arraycopy(plainText, 0, paddedPlainText, 0, plainText.length);

        // Fill the padding bytes with the padding size
        for (int i = plainText.length; i < paddedPlainText.length; i++) {
            paddedPlainText[i] = (byte) paddingSize;
        }

        // Perform the encryption
        byte[] cipherText = cipher.doFinal(paddedPlainText);

        // Base64 encode the cipher text, IV, and salt
        String encryptedData = Base64.getEncoder().encodeToString(cipherText) + "|" +
                               Base64.getEncoder().encodeToString(iv) + "|" +
                               Base64.getEncoder().encodeToString(salt);

        System.out.println("Encrypted data: " + encryptedData);
    }
}
