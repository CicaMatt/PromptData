import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    // Method to compute MD5 hash
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

    // Method to generate the IPB/MyBB style password hash
    public static String generatePasswordHash(String salt, String rawPassword) {
        // Hash the salt and the raw password individually
        String hashSalt = MD5(salt);
        String hashPassword = MD5(rawPassword);

        // Concatenate the hashed salt and hashed password, and hash them again
        return MD5(hashSalt + hashPassword);
    }

    public static void main(String[] args) {
        // Example values from your case
        String salt = "nzFC"; 
        // Replace with the actual raw password
        String rawPassword = "yourRawPassword"; 
        String dbHash = "6bac5cba673134ea084e481b57921134"; 

        // Generate the hash based on the salt and raw password
        String generatedHash = generatePasswordHash(salt, rawPassword);

        // Output for debugging purposes
        System.out.println("Generated Hash: " + generatedHash);
        System.out.println("Database Hash: " + dbHash);

        // Compare the generated hash with the stored hash
        if (generatedHash.equals(dbHash)) {
            System.out.println("Password matches!");
        } else {
            System.out.println("Password does not match.");
        }
    }
}
