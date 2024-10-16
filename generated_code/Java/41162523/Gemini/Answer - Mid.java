import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordChecker {

    public static void main(String[] args) {
        String storedPasswordHash = "f6ce584e7b4ff5253eed4a2ea2b44247"; // SHA-1 hash of the password

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String userInput = scanner.nextLine();
        scanner.close();

        String userInputHash = calculateSHA1Hash(userInput);

        if (userInputHash.equals(storedPasswordHash)) {
            System.out.println("Success");
        } else {
            System.out.println("Incorrect password");
        }
    }

    private static String calculateSHA1Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }
}