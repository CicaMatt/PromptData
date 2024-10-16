import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.KeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.Base64;

public class OpenSSLCompatibleEncryption {

    public static String encrypt(String toBeEncrypted, String password, String salt, int iterations) 
        throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, 
               IllegalBlockSizeException, BadPaddingException {

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName("UTF8")), iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes("UTF-8"));

        // OpenSSL-compatible output: Base64(ciphertext) + Base64(iv)
        return Base64.getEncoder().encodeToString(cipherText) + 
               Base64.getEncoder().encodeToString(iv); 
    }

    public static void main(String[] args) throws Exception {
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        String password = "passPhrase";
        String salt = "15charRandomSalt";
        int iterations = 100;

        String encryptedData = encrypt(toBeEncrypted, password, salt, iterations);
        System.out.println(encryptedData);
    }
}