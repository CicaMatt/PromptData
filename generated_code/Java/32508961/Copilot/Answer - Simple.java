import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OpenSSLEncryption {

    public static void main(String[] args) throws Exception {
        String password = "testpass";
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        byte[] salt = generateSalt();
        int iterations = 10000;

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Concatenate salt, IV, and ciphertext
        byte[] saltedPrefix = "Salted__".getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = new byte[saltedPrefix.length + salt.length + iv.length + cipherText.length];
        System.arraycopy(saltedPrefix, 0, encryptedData, 0, saltedPrefix.length);
        System.arraycopy(salt, 0, encryptedData, saltedPrefix.length, salt.length);
        System.arraycopy(iv, 0, encryptedData, saltedPrefix.length + salt.length, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, saltedPrefix.length + salt.length + iv.length, cipherText.length);

        // Encode to Base64
        String base64EncryptedData = Base64.getEncoder().encodeToString(encryptedData);
        System.out.println(base64EncryptedData);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
