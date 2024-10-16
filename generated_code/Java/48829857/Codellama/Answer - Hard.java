import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        decrypt(s, seed);
    }

    private static void decrypt(String s, String seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        String parts[] = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        // Convert the seed to a byte array using UTF-8 encoding
        byte[] secretBytes = seed.getBytes(StandardCharsets.UTF_8);

        // Create a MessageDigest instance and update it with the secret bytes
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(secretBytes);

        // Get the digest value as a byte array
        byte[] thedigest = md.digest();

        // Create a SecretKeySpec instance using the digest value
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");

        // Create a Cipher instance and initialize it with the secret key and IV string
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(ivString));
        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

        // Decrypt the encoded string using the Cipher instance
        byte[] output = cipher.doFinal(hexStringToByteArray(encodedString));

        System.out.println(new String(output));
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}