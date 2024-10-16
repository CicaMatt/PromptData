import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordComparison {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String pass = "f6ce584e7b4ff5253eed4a2ea2b44247"; // Encrypted password
        String userInput = "userinput"; // User input

        // Compare the encrypted password with the user input using SHA1
        if (MessageDigest.isEqual(pass, userInput)) {
            System.out.println("Success");
        } else {
            System.out.println("Failed to match");
        }
    }
}