import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EncryptionExample {

    public static void main(String[] args) throws Exception {

        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        char[] password = getPasswordFromConsole(); 
        byte[] salt = generateSecureSalt(); 
        int iterations = 65536;

        // Key derivation 
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Zero out the password characters after use
        for (int i = 0; i < password.length; i++) {
            password[i] = 0;
        }

        // Encryption using GCM mode
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); 
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] iv = cipher.getIV(); 
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Concatenate ciphertext, IV, and salt
        byte[] combinedData = new byte[cipherText.length + iv.length + salt.length];
        System.arraycopy(cipherText, 0, combinedData, 0, cipherText.length);
        System.arraycopy(iv, 0, combinedData, cipherText.length, iv.length);
        System.arraycopy(salt, 0, combinedData, cipherText.length + iv.length, salt.length);

        // Base64 encode the combined data
        String encryptedData = Base64.getEncoder().encodeToString(combinedData);

        System.out.println(encryptedData); 
    }

    // ... (rest of the helper functions remain the same)
}