import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SensitiveInformation {
    private static final String PASSWORD = "super_password";

    public static void main(String[] args) {
        byte[] passwordBytes = getPasswordBytes();
        // Use the bytes to make a secure API call or store the data in a secure location
        System.out.println("Password: " + new String(passwordBytes, StandardCharsets.UTF_8));
    }

    private static byte[] getPasswordBytes() {
        return Base64.getEncoder().encodeToString(PASSWORD.toCharArray()).getBytes(StandardCharsets.UTF_8);
    }
}