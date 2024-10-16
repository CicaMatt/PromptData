import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordChecker {

    public static void main(String[] args) {
        String storedPassHash = "f6ce584e7b4ff5253eed4a2ea2b44247"; // Stored SHA-1 hash

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password: ");
        String userInput = scanner.nextLine();
        scanner.close();

        String userInputHash = calculateSHA1(userInput);

        if (userInputHash.equals(storedPassHash)) {
            System.out.println("Success");
        } else {
            System.out.println("Incorrect password");
        }
    }

    public static String calculateSHA1(String input) {
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