import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {

    public static void main(String[] args) {
        String databaseSalt = "*nzFC "; // Note the trailing space
        String databaseHash = "6bac5cba673134ea084e481b57921134";
        String rawPassword = "your_password_here"; // Replace with the actual password

        String calculatedHash = calculateHash(databaseSalt, rawPassword);
        System.out.println("Calculated Hash: " + calculatedHash);

        if (calculatedHash.equals(databaseHash)) {
            System.out.println("Password matches!");
        } else {
            System.out.println("Password does not match.");
        }
    }

    public static String calculateHash(String salt, String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            // First MD5: Hash the salt
            byte[] saltHash = md.digest(salt.getBytes());

            // Second MD5: Hash the raw password
            byte[] passwordHash = md.digest(rawPassword.getBytes());

            // Concatenate the two hashes
            byte[] concatenatedHash = new byte[saltHash.length + passwordHash.length];
            System.arraycopy(saltHash, 0, concatenatedHash, 0, saltHash.length);
            System.arraycopy(passwordHash, 0, concatenatedHash, saltHash.length, passwordHash.length);

            // Third MD5: Hash the concatenated result
            byte[] finalHash = md.digest(concatenatedHash);

            // Convert to hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : finalHash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}