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

public class DecryptionExample {

    public static void main(String[] args) {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        String decrypted = decrypt(s, seed);
        System.out.println("Decrypted text: " + decrypted);
    }

    private static String decrypt(String s, String seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String parts[] = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        byte[] secretBytes = seed.getBytes(StandardCharsets.UTF_8);
        // Ensure the key is exactly 32 bytes long for AES-256
        SecretKey key = new SecretKeySpec(secretBytes, 0, 32, "AES"); 

        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(ivString));

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString); // Decode from hex
        byte[] output = cipher.doFinal(decodedBytes);

        return new String(output, StandardCharsets.UTF_8);
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