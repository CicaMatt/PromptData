import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static void main(String[] args) {
        String salt = "*nzFC";
        String rawPassword = "yourPassword";
        
        String serverGeneratedHash = generateHash(salt, rawPassword);
        System.out.println("Server generated hash: " + serverGeneratedHash);
    }

    public static String generateHash(String salt, String password) {
        String saltedPassword = MD5(MD5(salt) + MD5(password));
        return saltedPassword;
    }

    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
