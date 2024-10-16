import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordChecker {

    // Predefined SHA-1 encrypted password (hexadecimal format)
    static String storedPasswordHash = "f6ce584e7b4ff5253eed4a2ea2b44247";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your password: ");
        String userInput = scanner.nextLine();
        
        // Hash the user input using SHA-1 and compare it with the stored password hash
        if (storedPasswordHash.equals(hashPassword(userInput))) {
            System.out.println("Success");
        } else {
            System.out.println("Failure");
        }

        scanner.close();
    }

    // Method to hash the password using SHA-1
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Convert the byte array into a hexadecimal string
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
}
