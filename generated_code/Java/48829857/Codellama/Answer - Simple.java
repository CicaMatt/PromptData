import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Test {
    public static void main(String[] args) throws Exception {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        decrypt(s, seed);
    }

    private static void decrypt(String s, String seed) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String parts[] = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        byte[] secretBytes = seed.getBytes("UTF-8");
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(ivString));
        SecretKeySpec skey = new SecretKeySpec(secretBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
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