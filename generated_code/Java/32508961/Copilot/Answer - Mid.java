import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.Scanner;

public class OpenSSLCompatibleEncryption {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a secure passphrase: ");
        String password = scanner.nextLine();
        
        String toBeEncrypted = "AMOUNT=10&TID=#19:23&CURRENCY=EUR&LANGUAGE=DE&SUCCESS_URL=http://some.url/sucess&ERROR_URL=http://some.url/error&CONFIRMATION_URL=http://some.url/confirm&NAME=customer full name";
        int iterations = 10000;
        byte[] salt = generateSalt();
        
        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt the message using AES/GCM/NoPadding
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // GCM standard IV length is 12 bytes
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128-bit authentication tag length
        cipher.init(Cipher.ENCRYPT_MODE, secret, gcmSpec);
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Concatenate salt, IV, and ciphertext
        byte[] encryptedData = concatenate(salt, iv, cipherText);
        String base64EncryptedData = Base64.getEncoder().encodeToString(encryptedData);

        System.out.println(base64EncryptedData);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] concatenate(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }
}
