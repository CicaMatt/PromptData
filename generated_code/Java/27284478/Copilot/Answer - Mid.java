import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class SecurePasswordHandler {

    // This method securely converts a char[] password into a hashed byte array using PBKDF2
    public static byte[] hashPassword(char[] password, byte[] salt, int iterations, int keyLength) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        Arrays.fill(password, '\u0000'); // Clear the password char array after use
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        } finally {
            spec.clearPassword(); // Ensure sensitive data is cleared from memory
        }
    }

    public static void main(String[] args) {
        char[] password = "super_password".toCharArray();
        byte[] salt = "random_salt_value".getBytes();
        int iterations = 10000;
        int keyLength = 256;

        byte[] hashedPassword = hashPassword(password, salt, iterations, keyLength);

        // Use the hashedPassword securely (e.g., for authentication)
        System.out.println(Arrays.toString(hashedPassword));

        // Clear sensitive data
        Arrays.fill(hashedPassword, (byte) 0);
    }
}
