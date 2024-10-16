import java.security.MessageDigest;
import java.util.Base64;

class PasswordAuthentication {
    private static final String SALT = "*nzFC";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String rawPassword = "password";

        // Generate the hash using the IPBoard's password algorithm
        byte[] saltBytes = Base64.getDecoder().decode(SALT);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(saltBytes);
        byte[] hash = md.digest(rawPassword.getBytes());
        String ipboardHash = Base64.getEncoder().encodeToString(hash);

        // Print the results
        System.out.println("IPBoard Hash: " + ipboardHash);
    }
}