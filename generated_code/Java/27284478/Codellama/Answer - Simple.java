import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class PasswordEncryption {

    public static void main(String[] args) throws Exception {
        // create a secret key from a string
        String secret = "my_secret";
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");

        // encrypt the password in a byte array
        byte[] plaintext = "password".getBytes(StandardCharsets.UTF_8);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext);

        // store the encrypted password in a database or other storage

        // retrieve the encrypted password from a database or other storage
        // For demonstration, we reuse the encrypted byte array

        // decrypt the password
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedPlaintext = cipher.doFinal(encrypted);
        String password = new String(decryptedPlaintext, StandardCharsets.UTF_8);

        // Print decrypted password
        System.out.println("Decrypted Password: " + password);
    }
}
