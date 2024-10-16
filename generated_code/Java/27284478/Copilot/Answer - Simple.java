import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Arrays;

public class SecurePasswordHandling {

    public static void main(String[] args) {
        // Securely handle password
        char[] password = {'s', 'u', 'p', 'e', 'r', '_', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
        
        // Example: securely hashing the password before use
        byte[] salt = generateSalt();
        byte[] hashedPassword = hashPassword(password, salt);
        
        // Use the hashed password securely here (e.g., authentication)

        // Clear sensitive data after use
        Arrays.fill(password, '\0');  
        Arrays.fill(hashedPassword, (byte) 0);  
        Arrays.fill(salt, (byte) 0);  
    }
    
    // Function to securely hash the password using PBKDF2
    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);  // 65536 iterations, 256-bit key
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error during password hashing", e);
        }
    }
    
    // Generate a secure random salt
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];  // 128-bit salt
        random.nextBytes(salt);
        return salt;
    }
}
