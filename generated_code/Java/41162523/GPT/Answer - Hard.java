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

            // Hash the user input
            String inputPasswordHash = generateSHA1Hash(inputPassword);

            // Validate the password by comparing the hashes
            if (storedPasswordHash.equals(inputPasswordHash)) {
                System.out.println("Login successful");
            } else {
                throw new InvalidPasswordException("Invalid password");
            }

            scanner.close();
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());
        } catch (HashingException e) {
            System.out.println("Error while hashing the password: " + e.getMessage());
        }
    }

    // Method to hash input using SHA-1
    public static String generateSHA1Hash(String input) throws HashingException {
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
            throw new HashingException("SHA-1 algorithm not found", e);
        }
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
