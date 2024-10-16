import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class AesCtrDecryptor {

    public static void main(String[] args) throws Exception {
        // Example encrypted string from Node.js
        String encryptedData = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String secret = "d6F3231q7d1942874322a@123nab@392";

        // Decrypt the data
        String decryptedText = decrypt(encryptedData, secret);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    private static String decrypt(String encryptedData, String secret) throws Exception {
        // Split the IV and the encrypted text
        String[] parts = encryptedData.split(":");
        byte[] iv = hexStringToByteArray(parts[0]);
        byte[] encryptedText = hexStringToByteArray(parts[1]);

        // Ensure the secret key is 256 bits (32 bytes)
        byte[] key = getSHA256(secret);

        // Initialize Cipher with AES/CTR/NoPadding
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Initialize the cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(encryptedText);

        // Convert decrypted bytes to string
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static byte[] getSHA256(String text) throws Exception {
        // Hash the secret to get a 32-byte key (SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Arrays.copyOf(digest.digest(text.getBytes(StandardCharsets.UTF_8)), 32);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
