import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws Exception {
        String encryptedData = "94faa4f4a13cbff6d790183f3bdb3fb9:fae8b07a135e084eb91e";
        String seed = "d6F3231q7d1942874322a@123nab@392";
        decrypt(encryptedData, seed);
    }

    private static void decrypt(String encryptedData, String seed) throws Exception {
        String[] parts = encryptedData.split(":");
        String ivString = parts[0];
        String encryptedString = parts[1];

        byte[] iv = hexStringToByteArray(ivString);
        byte[] encryptedBytes = hexStringToByteArray(encryptedString);

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(seed.getBytes("UTF-8"));
        
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        System.out.println(new String(decryptedBytes));
    }

    // Helper method to convert hex string to byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
