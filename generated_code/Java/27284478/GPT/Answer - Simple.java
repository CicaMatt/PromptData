import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class SecurePasswordHandling {

    // Encrypt a password using AES and return the encrypted byte array
    public static byte[] encryptPassword(char[] password) throws Exception {
        // Convert char[] to byte[] securely
        byte[] passBytes = new String(password).getBytes("UTF-8");

        // Generate secret key from the password (hashing for simplicity, avoid hard-coded passwords in real apps)
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(passBytes);

        // Trim key to match AES 128-bit key length
        key = Arrays.copyOf(key, 16);

        // Create the secret key spec
        SecretKey secretKey = new SecretKeySpec(key, "AES");

        // Initialize cipher in encryption mode
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the password
        byte[] encryptedPassword = cipher.doFinal(passBytes);

        // Erase passBytes from memory after use
        Arrays.fill(passBytes, (byte) 0);

        return encryptedPassword;
    }

    public static void main(String[] args) {
        try {
            // Example password stored in char array (instead of String)
            char[] password = "super_secure_password".toCharArray();

            // Encrypt the password
            byte[] encryptedPassword = encryptPassword(password);

            // Erase the password from memory after use
            Arrays.fill(password, '\0');

            // Optionally, print or store the encrypted password securely
            System.out.println("Encrypted password: " + Arrays.toString(encryptedPassword));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
