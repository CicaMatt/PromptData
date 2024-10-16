import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class SecurePasswordHandling {

    public static void main(String[] args) {
        String password = "super_password";

        // Securely hash the password
        String hashedPassword = hashPassword(password);

        // Store or transmit the hashed password (not the original)
        System.out.println("Hashed password: " + hashedPassword);

        // When verifying a password, hash the input and compare to the stored hash
        String inputPassword = "another_password"; // User's attempt
        String hashedInput = hashPassword(inputPassword);
        boolean isValid = hashedInput.equals(hashedPassword);

        System.out.println("Is password valid? " + isValid);
    }

    private static String hashPassword(String password) {
        try {
            // Use a strong hashing algorithm with salt
            String algorithm = "PBKDF2WithHmacSHA256";
            int iterations = 65536; // Higher for better security, but impacts performance
            int keyLength = 256; // Length of the derived key in bits
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Clear sensitive data from memory
            spec.clearPassword();
            Arrays.fill(salt, (byte) 0);

            // Encode the hash and salt for storage
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}