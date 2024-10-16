import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws Exception {
        String encryptedData = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String secret = "d6F3231q7d1942874322a@123nab@392";

        decrypt(encryptedData, secret);
    }

    private static void decrypt(String encryptedData, String secret) throws Exception {
        String[] parts = encryptedData.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        // Derive a 256-bit key using SHA-256 from the secret string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        keyBytes = Arrays.copyOf(keyBytes, 32); 

        // Convert IV and encrypted text from hex string to byte array
        byte[] ivBytes = hexStringToByteArray(ivString);
        byte[] encryptedBytes = hexStringToByteArray(encodedString);

        // Create secret key and IV specs
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        // Initialize cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        // Perform decryption
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert decrypted bytes back to string
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    // Utility function to convert hex string to byte array
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
