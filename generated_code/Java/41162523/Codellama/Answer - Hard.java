import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordVerifier {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String userInput = "userinput";
        String encPassword = "f6ce584e7b4ff5253eed4a2ea2b44247";

        // Compare the entered password with the encrypted password
        if (encPassword.equals(hashPassword(userInput))) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return toHexString(digest);
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}