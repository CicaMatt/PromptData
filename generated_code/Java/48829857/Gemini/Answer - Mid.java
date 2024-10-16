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

public class DecryptExample {

    public static void main(String[] args) {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        decrypt(s, seed);
    }

    private static void decrypt(String s, String seed) {
        String[] parts = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        byte[] iv = hexStringToByteArray(ivString);
        SecretKeySpec skey = new SecretKeySpec(seed.getBytes(StandardCharsets.UTF_8), "AES");

        cipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));

        byte[] original = cipher.doFinal(hexStringToByteArray(encodedString));

        System.out.println(new String(original, StandardCharsets.UTF_8)); 
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}