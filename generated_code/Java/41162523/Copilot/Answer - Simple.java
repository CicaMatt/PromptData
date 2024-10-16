import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordCheck {

    // Function to hash the input string with SHA-1
    public static String sha1Hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // Example of a pre-hashed SHA-1 password
        String storedHashedPassword = "f6ce584e7b4ff5253eed4a2ea2b44247";

        // Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your password:");
        String userInput = scanner.nextLine();

        // Hash the user input and compare it to the stored hashed password
        String hashedInput = sha1Hash(userInput);
        if (storedHashedPassword.equals(hashedInput)) {
            System.out.println("success");
        } else {
            System.out.println("failure");
        }

        scanner.close();
    }
}
