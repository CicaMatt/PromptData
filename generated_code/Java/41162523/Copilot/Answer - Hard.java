import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordValidator {

    public static void main(String[] args) {
        // Stored SHA-1 hashed password
        String storedPasswordHash = "f6ce584e7b4ff5253eed4a2ea2b44247";

        try {
            // Get user input password
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your password: ");
            String inputPassword = scanner.nextLine();

            // Use PasswordUtils to hash the input
            String inputPasswordHash = PasswordUtils.hashPassword(inputPassword);

            // Validate the password
            if (PasswordUtils.isPasswordValid(inputPasswordHash, storedPasswordHash)) {
                System.out.println("Login successful");
            } else {
                throw new InvalidPasswordException("The password you entered is incorrect.");
            }

            scanner.close();
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());
        } catch (HashingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// Utility class for password-related operations
class PasswordUtils {

    // Method to hash input using SHA-1
    public static String hashPassword(String input) throws HashingException {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = sha1Digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new HashingException("Failed to hash password using SHA-1 algorithm", e);
        }
    }

    // Method to validate if the hashed user input matches the stored hash
    public static boolean isPasswordValid(String inputHash, String storedHash) {
        return inputHash.equals(storedHash);
    }
}

// Custom exception for invalid password
class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

// Custom exception for hashing errors
class HashingException extends Exception {
    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
