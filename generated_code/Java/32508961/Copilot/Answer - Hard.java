import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class OpenSSLCompatibleEncryption {
    public static void main(String[] args) throws Exception {
        String password = generateSecurePassword(32); // Secure random password
        String salt = generateSecureSalt(16); // Secure random salt
        int iterations = 10000; // Increased iterations for better security
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";

        // Derive the key, given password and salt
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt the message
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Concatenate salt, IV, and ciphertext
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = new byte[16 + iv.length + cipherText.length];
        System.arraycopy("Salted__".getBytes(StandardCharsets.UTF_8), 0, encryptedData, 0, 8);
        System.arraycopy(saltBytes, 0, encryptedData, 8, 8);
        System.arraycopy(iv, 0, encryptedData, 16, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, 16 + iv.length, cipherText.length);

        // Base64 encode the result
        String base64EncryptedData = Base64.getEncoder().encodeToString(encryptedData);
        System.out.println(base64EncryptedData);
    }

    public static String generateSecurePassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] passwordBytes = new byte[length];
        random.nextBytes(passwordBytes);
        return Base64.getEncoder().encodeToString(passwordBytes);
    }

    public static String generateSecureSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[length];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
