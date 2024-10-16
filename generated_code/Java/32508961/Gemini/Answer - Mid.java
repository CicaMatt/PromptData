import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class EncryptionExample {

    public static String encrypt(String toBeEncrypted, String password) throws Exception {
        // OpenSSL defaults (from the example)
        String salt = "Salted__"; // 8 bytes, fixed for OpenSSL
        int iterations = 2048; // Default for OpenSSL's enc command

        /* Derive the key, given password and salt. */
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName("UTF8")), iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        /* Encrypt the message. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes("UTF-8"));

        // Combine salt, iv, and ciphertext in OpenSSL's format
        ByteBuffer buffer = ByteBuffer.allocate(salt.length() + iv.length + cipherText.length);
        buffer.put(salt.getBytes());
        buffer.put(iv);
        buffer.put(cipherText);

        // Base64 encode the combined data
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    public static void main(String[] args) throws Exception {
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        String password = "testpass";

        String encryptedData = encrypt(toBeEncrypted, password);
        System.out.println(encryptedData);
    }
}