import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static void main(String[] args) {
        String salt = "*nzFC";
        String rawPassword = "yourPassword";
        
        String hashedPassword = hashPassword(salt, rawPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    }

    public static String hashPassword(String salt, String password) {
        return md5(md5(salt) + md5(password));
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
