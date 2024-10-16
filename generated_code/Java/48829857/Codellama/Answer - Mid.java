import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Test {
    public static void main(String[] args) throws Exception {
        String s = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";

        decrypt(s, seed);
    }

    private static void decrypt(String s, String seed)
            throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        String parts[] = s.split(":");
        String ivString = parts[0];
        String encodedString = parts[1];

        // Convert the ciphertext from Node.js to a hexadecimal string
        byte[] ciphertext = Hex.decode(encodedString);

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

        // Use the seed as the key for AES-256-CTR encryption
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] secretBytes = seed.getBytes("UTF-8");
        md.update(secretBytes);
        byte[] thedigest = md.digest();

        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");

        // Use the IV from Node.js for AES-256-CTR decryption
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(ivString));

        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
        byte[] output = cipher.doFinal(ciphertext);

        System.out.println(new String(output));
    }
}