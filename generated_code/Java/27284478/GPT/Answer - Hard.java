import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;

public class SecurePasswordHandler {

    private static final int SALT_LENGTH = 16; 
    private static final int HASH_ITERATIONS = 10000; 
    private static final int KEY_LENGTH = 256; 

    public static byte[] hashPassword(char[] password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, HASH_ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        // Clear password from memory after use
        spec.clearPassword(); 
        return hashedPassword;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static void main(String[] args) {
        char[] password = "super_password".toCharArray();
        try {
            // Step 1: Generate a random salt
            byte[] salt = generateSalt();

            // Step 2: Hash the password with the salt
            byte[] hashedPassword = hashPassword(password, salt);

            // For demonstration, we print the hashed password and salt (in real application, store these securely)
            System.out.println("Salt: " + Arrays.toString(salt));
            System.out.println("Hashed Password: " + Arrays.toString(hashedPassword));

            // Step 3: Clear sensitive data from memory
            Arrays.fill(password, '\u0000'); 
            Arrays.fill(hashedPassword, (byte) 0);
            Arrays.fill(salt, (byte) 0); 
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
