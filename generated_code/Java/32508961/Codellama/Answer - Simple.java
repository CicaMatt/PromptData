import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class EncryptMessage {
    public static void main(String[] args) throws Exception {
        String password = "passPhrase";
        String salt = "15charRandomSalt";
        int iterations = 100;

        // Derive the key, given password and salt.
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterations, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Encrypt the message.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes(StandardCharsets.UTF_8));

        // Base64-encode the output and concatenate it with the IV and salt.
        String encryptedData = new String(Base64.encodeBase64(cipherText), StandardCharsets.UTF_8) +
                new String(Base64.encodeBase64(iv), StandardCharsets.UTF_8) +
                new String(Base64.encodeBase64(salt.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        System.out.println(encryptedData);
    }
}