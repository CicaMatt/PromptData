import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class OpenSSLCompatibleEncryption {

    public static String encrypt(String toBeEncrypted, String passPhrase) throws Exception {
        // Generate salt (8 bytes)
        byte[] salt = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Number of iterations (the same as OpenSSL default)
        int iterations = 10000;

        // Derive the key using PBKDF2 with the same salt and iterations
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Initialize Cipher in AES/CBC/PKCS5Padding mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        // Encrypt the message
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Concatenate the output as OpenSSL does: "Salted__" + salt + iv + ciphertext
        byte[] prefix = "Salted__".getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = new byte[prefix.length + salt.length + iv.length + cipherText.length];
        System.arraycopy(prefix, 0, encryptedData, 0, prefix.length);
        System.arraycopy(salt, 0, encryptedData, prefix.length, salt.length);
        System.arraycopy(iv, 0, encryptedData, prefix.length + salt.length, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, prefix.length + salt.length + iv.length, cipherText.length);

        // Base64 encode the final result
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static void main(String[] args) throws Exception {
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        String passPhrase = "testpass";
        
        String encryptedData = encrypt(toBeEncrypted, passPhrase);
        System.out.println("Encrypted Data: " + encryptedData);
    }
}
