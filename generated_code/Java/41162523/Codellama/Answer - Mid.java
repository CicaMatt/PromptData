import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordCheck {
    
    // Method to hash a string using SHA-1
    public static String hashPassword(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // The stored SHA-1 encrypted password
        String encryptedPass = "f6ce584e7b4ff5253eed4a2ea2b44247";

        // Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String userInput = scanner.nextLine();

        try {
            // Hash the user input and compare with the stored encrypted password
            if (encryptedPass.equals(hashPassword(userInput))) {
                System.out.println("Success");
            } else {
                System.out.println("Failed");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
