import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class OpenSSLEncryptor {

    public static String encrypt(String toEncrypt, String passphrase) throws Exception {
        // Generate a random 8-byte salt
        byte[] salt = new byte[8];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);

        // Derive key and IV using PBKDF2 with the same iteration count and salt
        int iterations = 10000;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Create cipher instance with AES/CBC/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        
        // Get the IV from the cipher parameters
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        // Encrypt the message
        byte[] encryptedText = cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8));

        // Construct the final result mimicking OpenSSL's output
        // "Salted__" + salt + encrypted data
        byte[] saltedPrefix = "Salted__".getBytes(StandardCharsets.UTF_8);
        byte[] finalOutput = new byte[saltedPrefix.length + salt.length + iv.length + encryptedText.length];

        System.arraycopy(saltedPrefix, 0, finalOutput, 0, saltedPrefix.length);
        System.arraycopy(salt, 0, finalOutput, saltedPrefix.length, salt.length);
        System.arraycopy(iv, 0, finalOutput, saltedPrefix.length + salt.length, iv.length);
        System.arraycopy(encryptedText, 0, finalOutput, saltedPrefix.length + salt.length + iv.length, encryptedText.length);

        // Encode the final output to Base64
        return Base64.getEncoder().encodeToString(finalOutput);
    }

    public static void main(String[] args) {
        try {
            String toEncrypt = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/success&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
            String passphrase = "testpass";
            String encrypted = encrypt(toEncrypt, passphrase);
            System.out.println("Encrypted data: " + encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
