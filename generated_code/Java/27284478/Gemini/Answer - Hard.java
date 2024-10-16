import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class SecurePasswordHandling {

    public static void main(String[] args) {
        String password = "super_password";

        // Hash the password securely
        String hashedPassword = hashPassword(password);
        System.out.println("Hashed Password: " + hashedPassword);

        // Compare a new password attempt with the stored hash
        String newPasswordAttempt = "another_password";
        boolean isPasswordValid = verifyPassword(newPasswordAttempt, hashedPassword);
        System.out.println("Is password valid? " + isPasswordValid);
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static boolean verifyPassword(String newPasswordAttempt, String storedHash) {
        String newPasswordHash = hashPassword(newPasswordAttempt);
        return newPasswordHash.equals(storedHash);
    }
}