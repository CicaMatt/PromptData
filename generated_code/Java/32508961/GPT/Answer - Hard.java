import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class OpenSSLCompatibleEncryptionGCM {

    private static final int SALT_LENGTH = 8; // OpenSSL uses an 8-byte salt
    private static final int IV_LENGTH = 12;  // Recommended IV length for GCM (12 bytes)
    private static final int KEY_LENGTH = 256; // AES-256 requires a 256-bit key
    private static final int TAG_LENGTH = 128; // Authentication tag length (in bits)
    private static final int ITERATIONS = 10000; // Use a high iteration count

    public static String encrypt(String passphrase, String plaintext) throws Exception {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        // Derive the key using PBKDF2 (same as OpenSSL)
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Generate a random IV (initialization vector)
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);

        // Encrypt the plaintext using AES/GCM/NoPadding
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Prepend the salt (OpenSSL format starts with "Salted__" followed by the salt)
        byte[] saltedPrefix = "Salted__".getBytes(StandardCharsets.UTF_8);
        byte[] saltedOutput = new byte[saltedPrefix.length + salt.length + iv.length + ciphertext.length];

        System.arraycopy(saltedPrefix, 0, saltedOutput, 0, saltedPrefix.length);
        System.arraycopy(salt, 0, saltedOutput, saltedPrefix.length, salt.length);
        System.arraycopy(iv, 0, saltedOutput, saltedPrefix.length + salt.length, iv.length);
        System.arraycopy(ciphertext, 0, saltedOutput, saltedPrefix.length + salt.length + iv.length, ciphertext.length);

        // Base64 encode the final result (salt + IV + ciphertext)
        return Base64.getEncoder().encodeToString(saltedOutput);
    }

    public static void main(String[] args) {
        try {
            String passphrase = "testpass";
            String plaintext = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
            String encryptedText = encrypt(passphrase, plaintext);
            System.out.println("Encrypted Data: " + encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
