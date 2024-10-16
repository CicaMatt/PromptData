import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashChecker {

    public static void main(String[] args) {
        // Example data from the database
        String dbSalt = "*nzFC";
        String dbHash = "6bac5cba673134ea084e481b57921134";
        
        // Raw password to be tested
        String rawPassword = "your_password_here";

        // Generate the hash using the MD5(MD5(salt) + MD5(password)) format
        String generatedHash = generateHash(dbSalt, rawPassword);

        System.out.println("Database Hash: " + dbHash);
        System.out.println("Generated Hash: " + generatedHash);
        
        // Check if the generated hash matches the one stored in the database
        if (dbHash.equals(generatedHash)) {
            System.out.println("The hashes match!");
        } else {
            System.out.println("The hashes do not match.");
        }
    }

    // Function to generate the final hash using the salt and raw password
    public static String generateHash(String salt, String password) {
        String saltMd5 = MD5(salt);
        String passwordMd5 = MD5(password);
        String combined = saltMd5 + passwordMd5;
        return MD5(combined);
    }

    // Function to perform the MD5 hashing
    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
