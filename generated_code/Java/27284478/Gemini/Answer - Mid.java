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

        // Hashing the password using PBKDF2
        byte[] salt = generateSalt();
        byte[] hashedPassword = hashPassword(password.toCharArray(), salt);

        // Store salt and hashedPassword securely (e.g., in a database)

        // During authentication:
        String enteredPassword = "super_password"; // User input
        byte[] enteredPasswordHash = hashPassword(enteredPassword.toCharArray(), salt); // Retrieve salt

        // Compare hashes securely
        if (slowEquals(enteredPasswordHash, hashedPassword)) {
            System.out.println("Authentication successful");
        } else {
            System.out.println("Authentication failed");
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, 10000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        } finally {
            spec.clearPassword();
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}