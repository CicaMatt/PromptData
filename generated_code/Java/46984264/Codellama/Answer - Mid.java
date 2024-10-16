import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class CrossDatabaseEncryption {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;

    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    public static String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = new SecretKeySpec(generateKey().getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] combinedData = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combinedData, 0, iv.length);
        System.arraycopy(encryptedData, 0, combinedData, iv.length, encryptedData.length);
        return Base64.getEncoder().encodeToString(combinedData);
    }

    public static String decrypt(String cipherText) throws Exception {
        byte[] combinedData = Base64.getDecoder().decode(cipherText);
        byte[] iv = new byte[16];
        byte[] encryptedData = new byte[combinedData.length - 16];
        System.arraycopy(combinedData, 0, iv, 0, iv.length);
        System.arraycopy(combinedData, 16, encryptedData, 0, encryptedData.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = new SecretKeySpec(generateKey().getEncoded(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
    }
}
