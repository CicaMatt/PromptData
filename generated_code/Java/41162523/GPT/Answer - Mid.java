import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordCheck {

    public static void main(String[] args) {
        // The stored SHA-1 hashed password (example)
        String storedHash = "f6ce584e7b4ff5253eed4a2ea2b44247";

        // Get user input (unhashed password)
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String userInput = scanner.nextLine();

        // Hash the user input using SHA-1
        String hashedInput = sha1Hash(userInput);

        // Compare the hashed user input with the stored hash
        if (storedHash.equals(hashedInput)) {
            System.out.println("Success");
        } else {
            System.out.println("Failure");
        }

        scanner.close();
    }

    // Function to hash the input using SHA-1
    public static String sha1Hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert hashed bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
