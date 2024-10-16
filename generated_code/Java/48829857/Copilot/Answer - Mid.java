import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws Exception {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        decrypt(s, seed);
    }

    private static void decrypt(String s, String seed) throws Exception {
        String[] parts = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        byte[] iv = hexStringToByteArray(ivString);
        byte[] encryptedText = hexStringToByteArray(encodedString);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] key = Arrays.copyOf(md.digest(seed.getBytes(StandardCharsets.UTF_8)), 32);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

        byte[] original = cipher.doFinal(encryptedText);

        System.out.println(new String(original, StandardCharsets.UTF_8));
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
