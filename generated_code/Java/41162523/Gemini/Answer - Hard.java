import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SecurePasswordChecker {

    public static void main(String[] args) {
        // SHA-1 hash of the original password
        String storedPasswordHash = "f6ce584e7b4ff5253eed4a2ea2b44247"; 

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String userInput = scanner.nextLine();
        scanner.close();

        try {
            String userInputHash = calculateSHA1(userInput);

            if (userInputHash.equals(storedPasswordHash)) {
                System.out.println("Success! Password matches.");
            } else {
                System.out.println("Failure! Incorrect password.");
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: SHA-1 algorithm not available. This is critical, please investigate.");
            // Consider logging this error for further analysis
        }
    }

    private static String calculateSHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array to a hexadecimal string representation
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}