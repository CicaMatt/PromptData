import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DecryptionSolution {

    public static void main(String[] args) {
        String encryptedData = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String secretKey = "d6F3231q7d1942874322a@123nab@392";

        String decryptedText = decrypt(encryptedData, secretKey);
        System.out.println(decryptedText); 
    }

    private static String decrypt(String encryptedData, String secretKey) 
            throws NoSuchPaddingException, NoSuchAlgorithmException, 
                InvalidAlgorithmParameterException, InvalidKeyException {
        String[] parts = encryptedData.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        // Key Preparation: Directly use the secret key without additional hashing
        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(ivString));

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decodedBytes = cipher.doFinal(hexStringToByteArray(encodedString));

        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}